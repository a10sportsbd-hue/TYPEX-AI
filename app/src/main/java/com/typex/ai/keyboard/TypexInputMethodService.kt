package com.typex.ai.keyboard

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.inputmethodservice.InputMethodService
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import com.typex.ai.languages.WordSuggestionEngine
import com.typex.ai.languages.UserVocabularyStore
import com.typex.ai.languages.MixedLanguageEngine
import com.typex.ai.languages.SentencePredictionEngine
import com.typex.ai.languages.AdvancedAutocorrectEngine
import com.typex.ai.languages.AdvancedEnglishTypingEngine
import com.typex.ai.languages.PhraseCompletionEngine
import com.typex.ai.languages.TypingStatsStore
import com.typex.ai.languages.AdaptivePredictionRanker
import com.typex.ai.languages.SmartKeyboardIntelligence
import com.typex.ai.languages.SwipeTypingEngine
import com.typex.ai.languages.bengali.BengaliPhoneticEngine
import com.typex.ai.languages.bengali.BengaliKeyboardLayoutEngine
import com.typex.ai.languages.bengali.BengaliWordSuggestionEngine
import com.typex.ai.languages.bengali.BengaliCustomDictionaryStore
import com.typex.ai.languages.bengali.BengaliAutocorrectEngine
import com.typex.ai.themes.TypexThemes
import kotlin.math.roundToInt

/**
 * TYPEX AI Phase 2/3 IME.
 * English QWERTY and a first offline Bengali phonetic composition path.
 */
class TypexInputMethodService : InputMethodService() {

    private var root: LinearLayout? = null
    private var shifted = false
    private var capsLocked = false
    private var symbols = false
    private var bengaliMode = false
    private var currentEditorInputType: Int = EditorInfo.TYPE_CLASS_TEXT
    private val handler = Handler(Looper.getMainLooper())
    private val bengaliComposition = BengaliCompositionController()
    private val typedToken = StringBuilder()
    private var previousCommittedWord: String = ""
    private var sentenceStart = true
    private var customBengaliWords: Map<String, String> = emptyMap()
    private var learnedEnglishWords: Set<String> = emptySet()
    private var learnedEnglishScores: Map<String, Int> = emptyMap()
    private var learnedBengaliScores: Map<String, Int> = emptyMap()
    private val builtInBengaliWords by lazy { BengaliPhoneticEngine.dictionaryEntries() }
    private var oneHandMode = false
    private var clipboardPanelVisible = false
    private var emojiPanelVisible = false
    private val wordEngine = WordSuggestionEngine()
    private fun advancedEnglishEngine() =
        AdvancedEnglishTypingEngine(frequency = learnedEnglishScores)
    private val swipePath = StringBuilder()
    private var swipeMoved = false
    private var swipePreviewView: TextView? = null
    private val swipeEngine = SwipeTypingEngine()
    private val bengaliSwipeEngine by lazy {
        com.typex.ai.languages.bengali.BengaliSwipeTypingEngine(
            BengaliPhoneticEngine.dictionaryEntries() + BengaliCustomDictionaryStore.all(this)
        )
    }

    private var bg = Color.rgb(12, 12, 20)
    private var keyBg = Color.rgb(35, 34, 49)
    private var specialBg = Color.rgb(74, 61, 112)
    private var accent = Color.rgb(190, 169, 255)
    private var foreground = Color.rgb(248, 246, 255)
    private var keyHeightDp = 46
    private var keySpacingDp = 2
    private var keyTextSizeSp = 20
    private var keyCornerRadiusDp = 9

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        currentEditorInputType = attribute?.inputType ?: EditorInfo.TYPE_CLASS_TEXT
        bengaliComposition.clear()
        typedToken.clear()
        previousCommittedWord = ""
        refreshPersonalDictionaries()
        clipboardPanelVisible = false
        emojiPanelVisible = false
        if (isPasswordField(currentEditorInputType)) {
            bengaliMode = false
            shifted = false
            capsLocked = false
        }
    }

    private fun refreshPersonalDictionaries() {
        customBengaliWords = BengaliCustomDictionaryStore.all(this)
        learnedEnglishWords = UserVocabularyStore.all(this)
        learnedEnglishScores = UserVocabularyStore.scores(this)
        learnedBengaliScores = UserVocabularyStore.bengaliScores(this)
    }

    override fun onFinishInput() {
        commitBengaliBuffer()
        TypingStatsStore.flush(this)
        super.onFinishInput()
    }

    override fun onCreateInputView(): View {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(5), dp(8), dp(5), dp(8))
            setBackgroundColor(bg)
            layoutParams = ViewGroup.LayoutParams(
                if (oneHandMode) dp(TypexThemes.oneHandWidth(this@TypexInputMethodService)) else ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            gravity = if (oneHandMode) Gravity.END else Gravity.CENTER
        }
        renderKeyboard()
        return root!!
    }

    override fun onEvaluateFullscreenMode(): Boolean = false

    private fun renderKeyboard() {
        val theme = TypexThemes.selected(this)
        bg = theme.background.toInt()
        keyBg = theme.key.toInt()
        specialBg = theme.specialKey.toInt()
        foreground = theme.text.toInt()
        accent = theme.accent.toInt()
        keyHeightDp = TypexThemes.keyHeight(this)
        if (TypexThemes.compactMode(this)) keyHeightDp = (keyHeightDp - 6).coerceAtLeast(34)
        keySpacingDp = TypexThemes.keySpacing(this)
        keyTextSizeSp = TypexThemes.keyTextSize(this)
        keyCornerRadiusDp = TypexThemes.keyCornerRadius(this)

        val container = root ?: return
        container.setBackgroundColor(bg)
        container.setPadding(dp(TypexThemes.keyboardSideInset(this)), dp(8),
            dp(TypexThemes.keyboardSideInset(this)), dp(8))
        container.removeAllViews()

        swipePreviewView = null
        if (TypexThemes.toolbarEnabled(this)) addPhase8Toolbar(container)
        if (TypexThemes.swipePreviewEnabled(this) && !symbols && !emojiPanelVisible && !clipboardPanelVisible) {
            val preview = TextView(this).apply {
                text = "Swipe across keys to type"
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(accent)
                setPadding(dp(4), 0, dp(4), 0)
                visibility = View.GONE
            }
            swipePreviewView = preview
            container.addView(preview, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(22)
            ))
        }
        if (emojiPanelVisible) { addEmojiSearchPanel(container); return }
        if (clipboardPanelVisible) { addClipboardHistoryPanel(container); return }
        if (symbols) {
            addRow(container, listOf("1","2","3","4","5","6","7","8","9","0"))
            addRow(container, listOf("@","#","$","%","&","-","+","(",")","/"))
            addRow(container, listOf("=","*","\"",":",";","!","?","'","⌫"))
        } else {
            val rows = if (bengaliMode) BengaliKeyboardLayoutEngine.rows(TypexThemes.bengaliLayout(this))
                else KeyboardLayoutEngine.rows(TypexThemes.keyboardLayout(this))
            rows.forEach { row -> addRow(container, row) }
        }
        addBottomRows(container)
    }

    private fun addPhase8Toolbar(parent: LinearLayout) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(34)
            ).apply { bottomMargin = dp(4) }
        }
        addBottomKey(row, "🎙", 0.8f, specialBg) {
            // Explicit tap launches the app's voice dictation screen.
            try {
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                intent?.putExtra("open_voice_dictation", true)
                if (intent != null) startActivity(intent)
            } catch (_: Exception) { }
        }
        addBottomKey(row, "📋", 0.9f, keyBg) {
            clipboardPanelVisible = !clipboardPanelVisible
            emojiPanelVisible = false
            renderKeyboard()
        }
        addBottomKey(row, "😊", 0.9f, keyBg) {
            emojiPanelVisible = !emojiPanelVisible
            clipboardPanelVisible = false
            renderKeyboard()
        }
        addBottomKey(row, "✨", 0.9f, specialBg) {
            try {
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent?.putExtra("open_ai_studio", true)
                if (intent != null) startActivity(intent)
            } catch (_: Exception) { }
        }
        addBottomKey(row, if (bengaliMode) BengaliKeyboardLayoutEngine.layoutName(TypexThemes.bengaliLayout(this)) else if (oneHandMode) "↔" else "⇤", 0.8f, specialBg) {
            if (bengaliMode) {
                val layouts = BengaliKeyboardLayoutEngine.supported
                val current = BengaliKeyboardLayoutEngine.normalize(TypexThemes.bengaliLayout(this))
                val nextLayout = layouts[(layouts.indexOf(current) + 1) % layouts.size]
                commitBengaliBuffer()
                TypexThemes.saveBengaliLayout(this, nextLayout)
                renderKeyboard()
            } else {
                oneHandMode = !oneHandMode
                root?.layoutParams = root?.layoutParams?.apply {
                    width = if (oneHandMode) dp(TypexThemes.oneHandWidth(this@TypexInputMethodService)) else ViewGroup.LayoutParams.MATCH_PARENT
                }
                root?.requestLayout()
            }
        }
        addBottomKey(row, "ABC", 0.8f, keyBg) { symbols = false; renderKeyboard() }
        parent.addView(row)
    }

    private fun addEmojiSearchPanel(parent: LinearLayout) {
        val search = EditText(this).apply {
            hint = "Search emoji: love, হাসি, খাবার, বৃষ্টি..."
            setSingleLine(true); textSize = 13f
            setTextColor(this@TypexInputMethodService.foreground); setHintTextColor(this@TypexInputMethodService.foreground)
            background = rounded(keyBg, dp(9).toFloat())
            setPadding(dp(10), 0, dp(10), 0)
        }
        parent.addView(search, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(40)))
        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        parent.addView(grid)
        fun draw(query: String) {
            grid.removeAllViews()
            val matches = EmojiSearchEngine.search(query, 32)
            matches.map { it.emoji }.chunked(8).forEach { chunk ->
                val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
                chunk.forEach { emoji ->
                    val key = makeKey(emoji, keyBg).apply {
                        textSize = 21f
                        setOnClickListener { currentInputConnection?.commitText(emoji, 1) }
                    }
                    row.addView(key, LinearLayout.LayoutParams(0, dp(40), 1f).apply {
                        setMargins(dp(2), dp(2), dp(2), dp(2))
                    })
                }
                grid.addView(row)
            }
            if (matches.isEmpty()) {
                grid.addView(TextView(this).apply {
                    text = "No emoji found"
                    setTextColor(this@TypexInputMethodService.foreground); gravity = Gravity.CENTER
                    setPadding(0, dp(12), 0, dp(12))
                })
            }
        }
        draw("")
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { draw(s?.toString().orEmpty()) }
            override fun afterTextChanged(s: Editable?) {}
        })
        addPanelClose(parent) { emojiPanelVisible = false; renderKeyboard() }
    }

    private fun addClipboardHistoryPanel(parent: LinearLayout) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val save = TextView(this).apply {
            text = "＋ Save current clipboard to history"; gravity = Gravity.CENTER
            setTextColor(accent)
            setOnClickListener {
                val clip = clipboard.primaryClip
                if (clip != null && clip.itemCount > 0) {
                    ClipboardHistoryStore.add(this@TypexInputMethodService,
                        clip.getItemAt(0).coerceToText(this@TypexInputMethodService).toString())
                    renderKeyboard()
                }
            }
        }
        parent.addView(save, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(38)))
        ClipboardHistoryStore.all(this).take(6).forEach { item ->
            val entry = TextView(this).apply {
                text = item.take(90); setTextColor(this@TypexInputMethodService.foreground); setPadding(dp(8), dp(6), dp(8), dp(6))
                background = rounded(keyBg, dp(8).toFloat())
                setOnClickListener {
                    currentInputConnection?.commitText(item, 1)
                    clipboardPanelVisible = false; renderKeyboard()
                }
            }
            parent.addView(entry, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(38))
                .apply { bottomMargin = dp(3) })
        }
        val clear = TextView(this).apply {
            text = "Clear history"; gravity = Gravity.CENTER; setTextColor(Color.RED)
            setOnClickListener { ClipboardHistoryStore.clear(this@TypexInputMethodService); renderKeyboard() }
        }
        parent.addView(clear, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(32)))
        addPanelClose(parent) { clipboardPanelVisible = false; renderKeyboard() }
    }

    private fun addPanelClose(parent: LinearLayout, close: () -> Unit) {
        val button = TextView(this).apply {
            text = "ABC  •  Close panel"; gravity = Gravity.CENTER; setTextColor(accent)
            setOnClickListener { close() }
        }
        parent.addView(button, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(32)))
    }

    private fun displayKey(key: String): String =
        if (shifted || capsLocked) key.uppercase() else key

    private fun addRow(parent: LinearLayout, keys: List<String>) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(keyHeightDp + 3)
            ).apply { bottomMargin = dp(6) }
        }
        keys.forEach { logicalKey ->
            val label = if (bengaliMode) BengaliKeyboardLayoutEngine.displayForKey(
                TypexThemes.bengaliLayout(this), logicalKey, shifted || capsLocked
            ) else displayKey(logicalKey)
            val weight = if (logicalKey == "⇧" || logicalKey == "⌫") 1.35f else 1f
            row.addView(makeKey(label, actionLabel = logicalKey), LinearLayout.LayoutParams(0, dp(keyHeightDp), weight).apply {
                marginStart = dp(keySpacingDp)
                marginEnd = dp(keySpacingDp)
            })
        }
        parent.addView(row)
    }

    private fun addBottomRows(parent: LinearLayout) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(keyHeightDp + 3)
            ).apply { bottomMargin = dp(2) }
        }
        addBottomKey(row, if (symbols) "ABC" else "?123", 1.2f, specialBg) {
            symbols = !symbols
            renderKeyboard()
        }
        addBottomKey(row, if (bengaliMode) "EN" else "বাংলা", 1.0f, specialBg) {
            commitBengaliBuffer()
            bengaliMode = !bengaliMode
            symbols = false
            renderKeyboard()
        }
        addBottomKey(row, ",", 0.75f, keyBg) { commitPunctuation(",") }
        addBottomKey(row, if (bengaliMode) "স্পেস" else "space", 3.6f, keyBg) {
            commitBengaliBuffer()
            currentInputConnection?.commitText(" ", 1)
            if (shifted && !capsLocked) {
                shifted = false
                renderKeyboard()
            }
        }
        addBottomKey(row, ".", 0.75f, keyBg) { commitPunctuation(".") }
        addBottomKey(row, "↵", 1.2f, specialBg) { commitBengaliBuffer(); performEnter() }
        parent.addView(row)

        if (bengaliMode && !bengaliComposition.isEmpty()) {
            val suggestions = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(34)
                )
            }
            val customWords = customBengaliWords
            val mergedDictionary = builtInBengaliWords + customWords
            val learnedEnglish = learnedEnglishWords
            val romanInput = bengaliComposition.raw()
            val bengaliEngine = BengaliWordSuggestionEngine(mergedDictionary)
            val pipelineResult = com.typex.ai.languages.bengali.BengaliTypingPipeline.analyze(
                romanInput,
                previousBengaliWord = previousCommittedWord.takeIf { it.isNotBlank() },
                limit = 5
            )
            val candidates = (bengaliEngine.suggest(
                romanInput,
                previousBengaliWord = previousCommittedWord.takeIf { it.isNotBlank() },
                limit = 5
            ) + pipelineResult.alternatives).toMutableList().apply {
                val mixedCandidate = MixedLanguageEngine.resolveBengaliModeToken(
                    romanInput, customWords, learnedEnglish
                )
                if (mixedCandidate.isNotBlank() && mixedCandidate !in this) add(0, mixedCandidate)
            }.distinct()
            val rankedBengaliCandidates = AdaptivePredictionRanker.rank(
                candidates, learnedBengaliScores, limit = 5
            )
            val bnCorrection = BengaliAutocorrectEngine(mergedDictionary).candidate(romanInput)
            if (bnCorrection != null && candidates.none { it == bnCorrection.second }) {
                addSuggestion(suggestions, "সংশোধন: ${bnCorrection.second}", true) {
                    currentInputConnection?.commitText(bnCorrection.second, 1)
                    if (!isPasswordField(currentEditorInputType)) TypingStatsStore.recordSuggestion(this)
                    previousCommittedWord = bnCorrection.second
                    bengaliComposition.clear()
                    renderKeyboard()
                }
            }
            if (rankedBengaliCandidates.isEmpty() && bnCorrection == null) {
                addSuggestion(suggestions, "বাংলা Phonetic", false) {}
            } else {
                rankedBengaliCandidates.forEachIndexed { index, candidate ->
                    addSuggestion(suggestions, candidate, index == 0, onLongPress = {
                        if (bengaliComposition.raw().isNotBlank()) {
                            BengaliCustomDictionaryStore.put(this, bengaliComposition.raw(), candidate)
                            customBengaliWords = BengaliCustomDictionaryStore.all(this)
                            renderKeyboard()
                        }
                    }) {
                        currentInputConnection?.commitText(candidate, 1)
                        if (!isPasswordField(currentEditorInputType)) {
                            TypingStatsStore.recordSuggestion(this)
                            UserVocabularyStore.recordAcceptedBengali(this, candidate)
                            learnedBengaliScores = UserVocabularyStore.bengaliScores(this)
                        }
                        previousCommittedWord = candidate
                        bengaliComposition.clear()
                        renderKeyboard()
                    }
                }
            }
            parent.addView(suggestions)
        } else if (!isPasswordField(currentEditorInputType) && typedToken.isNotEmpty()) {
            val suggestions = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(32))
            }
            val learnedWords = learnedEnglishWords.sortedWith(
                compareByDescending<String> { learnedEnglishScores[it] ?: 0 }.thenBy { it }
            )
            val personalEngine = WordSuggestionEngine(
                WordSuggestionEngine.DEFAULT_WORDS + learnedWords,
                learnedEnglishScores
            )
            val token = typedToken.toString()
            val advanced = advancedEnglishEngine().analyze(
                token,
                previousWords = listOf(previousCommittedWord).filter(String::isNotBlank),
                limit = 4
            )
            val predictions = personalEngine.predict(token, 4) + advanced.predictions
            val correction = advanced.correction ?: personalEngine.correct(token)
            val contraction = advanced.expandedContraction
            val candidates = AdaptivePredictionRanker.rank(
                (listOfNotNull(contraction) + predictions + listOfNotNull(correction)).distinct(),
                learnedEnglishScores,
                limit = 5
            )
            candidates.forEach { candidate ->
                val isCorrection = candidate == correction || candidate == contraction
                addSuggestion(suggestions, candidate, isCorrection,
                    onLongPress = {
                        UserVocabularyStore.add(this, candidate)
                        refreshPersonalDictionaries()
                        renderKeyboard()
                    }) {
                    UserVocabularyStore.recordAccepted(this, candidate)
                    if (!isPasswordField(currentEditorInputType)) TypingStatsStore.recordSuggestion(this)
                    refreshPersonalDictionaries()
                    val connection = currentInputConnection
                    connection?.deleteSurroundingText(typedToken.length, 0)
                    connection?.commitText(candidate, 1)
                    typedToken.clear(); typedToken.append(candidate.lowercase())
                    renderKeyboard()
                }
            }
            parent.addView(suggestions)
        } else if (!isPasswordField(currentEditorInputType) && previousCommittedWord.isNotBlank()) {
            val suggestions = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(34)
                )
            }
            val correction = if (!bengaliMode) {
                AdvancedAutocorrectEngine().englishCandidate(previousCommittedWord)
            } else null
            if (correction != null) {
                addSuggestion(suggestions, "Fix: $correction", true) {
                    val connection = currentInputConnection
                    connection?.deleteSurroundingText(previousCommittedWord.length + 1, 0)
                    connection?.commitText("$correction ", 1)
                    previousCommittedWord = correction
                    if (!isPasswordField(currentEditorInputType)) TypingStatsStore.recordSuggestion(this)
                    renderKeyboard()
                }
            }
            val phrases = PhraseCompletionEngine.completions(previousCommittedWord, bengaliMode, 3)
            val legacyNextWords = SentencePredictionEngine.nextWords(previousCommittedWord, bengaliMode, 3)
            val advancedNextWords = if (!bengaliMode) {
                advancedEnglishEngine().predictNext(listOf(previousCommittedWord), 4)
            } else emptyList()
            val punctuationHints = SmartKeyboardIntelligence.punctuationSuggestions(
                previousCommittedWord, bengaliMode, 3
            )
            (phrases + legacyNextWords + advancedNextWords + punctuationHints).distinct()
                .take(if (correction != null) 2 else 5).forEach { candidate ->
                addSuggestion(suggestions, candidate, false) {
                    currentInputConnection?.commitText(candidate + " ", 1)
                    if (!isPasswordField(currentEditorInputType)) TypingStatsStore.recordSuggestion(this)
                    previousCommittedWord = candidate
                    renderKeyboard()
                }
            }
            if (suggestions.childCount > 0) parent.addView(suggestions)
        }
    }

    private fun addSuggestion(
        parent: LinearLayout,
        text: String,
        emphasized: Boolean,
        onLongPress: (() -> Unit)? = null,
        action: () -> Unit
    ) {
        val item = TextView(this).apply {
            this.text = text
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(if (emphasized) this@TypexInputMethodService.accent else this@TypexInputMethodService.foreground)
            setPadding(dp(5), 0, dp(5), 0)
            setOnClickListener { action() }
            if (onLongPress != null) setOnLongClickListener {
                onLongPress()
                true
            }
        }
        parent.addView(item, LinearLayout.LayoutParams(0, dp(32), 1f))
    }

    private fun addBottomKey(
        row: LinearLayout,
        label: String,
        weight: Float,
        color: Int,
        action: () -> Unit
    ) {
        val key = makeKey(label, color)
        key.setOnClickListener {
            if (TypexThemes.vibrationEnabled(this@TypexInputMethodService)) {
                key.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
            }
            if (TypexThemes.keySoundEnabled(this@TypexInputMethodService)) {
                key.playSoundEffect(android.view.SoundEffectConstants.CLICK)
            }
            action()
        }
        row.addView(key, LinearLayout.LayoutParams(0, dp(keyHeightDp), weight).apply {
            marginStart = dp(keySpacingDp)
            marginEnd = dp(keySpacingDp)
        })
    }

    private fun makeKey(label: String, color: Int = keyBg, actionLabel: String = label): TextView {
        return TextView(this).apply {
            text = label
            textSize = if (label.length == 1) keyTextSizeSp.toFloat() else (keyTextSizeSp - 8).toFloat()
            setTextColor(this@TypexInputMethodService.foreground)
            gravity = Gravity.CENTER
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            background = rounded(color, dp(keyCornerRadiusDp).toFloat())
            isClickable = true
            isFocusable = true
            setOnClickListener {
                if (TypexThemes.keyAnimationsEnabled(this@TypexInputMethodService)) {
                    animate().scaleX(0.96f).scaleY(0.96f).setDuration(55).withEndAction {
                        animate().scaleX(1f).scaleY(1f).setDuration(75).start()
                    }.start()
                }
                if (TypexThemes.vibrationEnabled(this@TypexInputMethodService)) {
                    performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                }
                if (TypexThemes.keySoundEnabled(this@TypexInputMethodService)) {
                    playSoundEffect(android.view.SoundEffectConstants.CLICK)
                }
                handleKey(actionLabel)
            }
            if (actionLabel == "⌫") {
                setOnTouchListener { view, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.isPressed = true
                            startDeleteRepeat()
                            true
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            view.isPressed = false
                            handler.removeCallbacksAndMessages(null)
                            if (event.action == MotionEvent.ACTION_UP) deleteOne()
                            true
                        }
                        else -> true
                    }
                }
            }
            else if (actionLabel.length == 1 && actionLabel[0].isAsciiLetter()) {
                setOnTouchListener { view, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (TypexThemes.keyAnimationsEnabled(this@TypexInputMethodService)) {
                                view.animate().scaleX(0.94f).scaleY(0.94f).setDuration(55).start()
                            }
                            swipePath.setLength(0)
                            swipeMoved = false
                            swipePath.append(actionLabel.lowercase())
                            updateSwipePreview()
                            true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val hit = findLetterAt(event.rawX, event.rawY)
                            if (hit != null && hit != swipePath.lastOrNull()?.toString() && swipePath.length < 32) {
                                swipePath.append(hit)
                                swipeMoved = true
                                updateSwipePreview()
                            }
                            true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (TypexThemes.keyAnimationsEnabled(this@TypexInputMethodService)) {
                                view.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                            }
                            val canSwipe = !symbols && !isPasswordField(currentEditorInputType) &&
                                (!bengaliMode || BengaliKeyboardLayoutEngine.isPhonetic(TypexThemes.bengaliLayout(this))) &&
                                swipeMoved && swipePath.length >= 3
                            val candidate = if (canSwipe && bengaliMode) {
                                bengaliSwipeEngine.candidates(swipePath.toString(), 1).firstOrNull()?.second
                            } else if (canSwipe) {
                                swipeEngine.candidates(swipePath.toString(), 1).firstOrNull()
                            } else null
                            if (candidate != null) {
                                if (bengaliMode) {
                                    bengaliComposition.clear()
                                    currentInputConnection?.setComposingText("", 1)
                                }
                                currentInputConnection?.commitText(candidate, 1)
                                typedToken.clear()
                                typedToken.append(candidate)
                                if (TypexThemes.vibrationEnabled(this@TypexInputMethodService)) {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                }
                                renderKeyboard()
                            } else {
                                handleKey(actionLabel)
                            }
                            swipePath.setLength(0)
                            swipeMoved = false
                            updateSwipePreview()
                            true
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            if (TypexThemes.keyAnimationsEnabled(this@TypexInputMethodService)) {
                                view.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                            }
                            swipePath.setLength(0)
                            swipeMoved = false
                            updateSwipePreview()
                            true
                        }
                        else -> true
                    }
                }
            }
        }
    }

    private fun updateSwipePreview() {
        val preview = swipePreviewView ?: return
        if (!TypexThemes.swipePreviewEnabled(this)) return
        if (swipePath.isEmpty()) {
            preview.visibility = View.GONE
            preview.text = ""
        } else {
            preview.visibility = View.VISIBLE
            preview.text = if (bengaliMode) "বাংলা Swipe: ${swipePath}" else "Swipe: ${swipePath}"
        }
    }

    private fun findLetterAt(rawX: Float, rawY: Float): String? {
        val container = root ?: return null
        for (rowIndex in 0 until container.childCount) {
            val row = container.getChildAt(rowIndex)
            if (row !is LinearLayout) continue
            for (keyIndex in 0 until row.childCount) {
                val key = row.getChildAt(keyIndex)
                if (key !is TextView) continue
                val value = key.text?.toString().orEmpty()
                if (value.length != 1 || !value[0].isAsciiLetter()) continue
                val location = IntArray(2)
                key.getLocationOnScreen(location)
                val inside = rawX >= location[0] && rawX <= location[0] + key.width &&
                    rawY >= location[1] && rawY <= location[1] + key.height
                if (inside) return value.lowercase()
            }
        }
        return null
    }

    private fun handleKey(label: String) {
        when (label) {
            "⇧" -> {
                if (shifted) capsLocked = !capsLocked
                shifted = !shifted
                renderKeyboard()
            }
            "⌫" -> deleteOne()
            else -> {
                if (!isPasswordField(currentEditorInputType) &&
                    (label.length == 1 && label[0].isLetterOrDigit() || label == " ")
                ) TypingStatsStore.recordKey(this)
                if (bengaliMode && !symbols && BengaliKeyboardLayoutEngine.isPhonetic(TypexThemes.bengaliLayout(this)) &&
                    label.length == 1 && label[0].isLetter()
                ) {
                    appendRoman(label.lowercase())
                } else if (bengaliMode && !symbols && !BengaliKeyboardLayoutEngine.isPhonetic(TypexThemes.bengaliLayout(this)) &&
                    label.length == 1 && label[0].isLetter()
                ) {
                    val output = BengaliKeyboardLayoutEngine.outputForKey(
                        TypexThemes.bengaliLayout(this), label, shifted || capsLocked
                    )
                    if (output != null) currentInputConnection?.commitText(output, 1)
                } else {
                    commitBengaliBuffer()
                    if (label == " " || label in listOf(".", ",", "!", "?", "\\n")) {
                        expandShortcutBeforeBoundary()
                        if (typedToken.isNotEmpty()) {
                            previousCommittedWord = typedToken.toString()
                            if (!isPasswordField(currentEditorInputType)) TypingStatsStore.recordWord(this)
                        }
                    }
                    val outputLabel = SmartKeyboardIntelligence.capitalizeAtSentenceStart(
                        label, sentenceStart, bengaliMode
                    )
                    commit(outputLabel)
                    sentenceStart = SmartKeyboardIntelligence.nextSentenceStartAfter(label, sentenceStart)
                    if (label == " " || label in listOf(".", ",", "!", "?", "\\n")) typedToken.clear()
                    else if (label.length == 1 && label[0].isLetterOrDigit()) typedToken.append(label.lowercase())
                }
                renderKeyboard()
                if (shifted && !capsLocked) {
                    shifted = false
                    renderKeyboard()
                }
            }
        }
    }

    private fun appendRoman(value: String) {
        bengaliComposition.append(value, customBengaliWords, learnedEnglishWords)
        val composing = bengaliComposition.preview(customBengaliWords, learnedEnglishWords)
        currentInputConnection?.setComposingText(composing, 1)
    }

    private fun commitBengaliBuffer() {
        if (bengaliComposition.isEmpty()) return
        val converted = bengaliComposition.commit(customBengaliWords, learnedEnglishWords)
        currentInputConnection?.commitText(converted, 1)
        typedToken.setLength(0)
        typedToken.append(converted)
    }

    private fun expandShortcutBeforeBoundary() {
        if (isPasswordField(currentEditorInputType) || typedToken.isEmpty()) return
        val trigger = typedToken.toString()
        val expansion = com.typex.ai.keyboard.TextShortcutStore.all(this)[trigger] ?: return
        val connection = currentInputConnection ?: return
        connection.deleteSurroundingText(trigger.length, 0)
        connection.commitText(expansion, 1)
    }

    private fun commitPunctuation(value: String) {
        commitBengaliBuffer()
        currentInputConnection?.commitText(value, 1)
    }

    private fun commit(value: String) {
        currentInputConnection?.commitText(value, 1)
    }

    private fun deleteOne() {
        if (bengaliMode && !bengaliComposition.isEmpty()) {
            bengaliComposition.backspace()
            if (bengaliComposition.isEmpty()) {
                currentInputConnection?.setComposingText("", 1)
            } else {
                currentInputConnection?.setComposingText(
                    MixedLanguageEngine.resolveBengaliModeToken(
                        bengaliComposition.raw(), customBengaliWords, learnedEnglishWords
                    ), 1
                )
            }
            renderKeyboard()
            return
        }
        val connection = currentInputConnection ?: return
        val selected = connection.getSelectedText(0)
        if (!selected.isNullOrEmpty()) connection.commitText("", 1)
        else {
            if (!connection.deleteSurroundingTextInCodePoints(1, 0)) {
                connection.deleteSurroundingText(1, 0)
            }
        }
    }

    private fun startDeleteRepeat() {
        handler.removeCallbacksAndMessages(null)
        val repeat = object : Runnable {
            override fun run() {
                deleteOne()
                handler.postDelayed(this, 65)
            }
        }
        handler.postDelayed(repeat, 420)
    }

    private fun performEnter() {
        val connection = currentInputConnection ?: return
        val action = currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION)
            ?: EditorInfo.IME_ACTION_NONE
        val handled = when (action) {
            EditorInfo.IME_ACTION_GO,
            EditorInfo.IME_ACTION_SEARCH,
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_NEXT,
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_PREVIOUS -> connection.performEditorAction(action)
            else -> false
        }
        if (!handled) connection.commitText("\n", 1)
    }

    private fun isPasswordField(inputType: Int): Boolean {
        val variation = inputType and EditorInfo.TYPE_MASK_VARIATION
        val cls = inputType and EditorInfo.TYPE_MASK_CLASS
        return (cls == EditorInfo.TYPE_CLASS_TEXT &&
            (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
             variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
             variation == EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)) ||
            (cls == EditorInfo.TYPE_CLASS_NUMBER &&
             variation == EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    private fun rounded(color: Int, radius: Float): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }

    private fun Char.isAsciiLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).roundToInt()

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
