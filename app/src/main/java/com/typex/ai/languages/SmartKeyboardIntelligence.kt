package com.typex.ai.languages

/**
 * Phase 26 deterministic keyboard intelligence helpers.
 * These are local heuristics, not a generative or trained language model.
 */
object SmartKeyboardIntelligence {

    private val englishQuestionWords = setOf(
        "who", "what", "where", "when", "why", "how", "which"
    )
    private val bengaliQuestionWords = setOf(
        "কি", "কী", "কেন", "কোথায়", "কিভাবে", "কীভাবে", "কে", "কখন"
    )

    /** Suggests punctuation chips; never inserts punctuation automatically. */
    fun punctuationSuggestions(previousWord: String, bengaliMode: Boolean, limit: Int = 3): List<String> {
        if (limit <= 0 || previousWord.isBlank()) return emptyList()
        val word = previousWord.trim().lowercase()
        val isQuestion = if (bengaliMode) word in bengaliQuestionWords
            else word in englishQuestionWords
        val choices = if (isQuestion) listOf("?", ".", "!") else listOf(".", "!", "?")
        return choices.take(limit)
    }

    /** Sentence-start capitalization is only applied to English alphabetic keys. */
    fun capitalizeAtSentenceStart(
        key: String,
        sentenceStart: Boolean,
        bengaliMode: Boolean
    ): String {
        if (!sentenceStart || bengaliMode || key.length != 1 || !key[0].isLetter()) return key
        return key.uppercase()
    }

    fun nextSentenceStartAfter(input: String, current: Boolean): Boolean {
        return when (input) {
            ".", "!", "?", "\n" -> true
            " " -> current
            else -> if (input.any { it.isLetterOrDigit() }) false else current
        }
    }

    /** Basic deterministic cleanup for a user-selected sentence completion. */
    fun normalizeCompletion(text: String, sentenceStart: Boolean, bengaliMode: Boolean): String {
        if (text.isEmpty()) return text
        if (bengaliMode || !sentenceStart) return text
        val first = text.indexOfFirst { it.isLetter() }
        if (first < 0) return text
        return text.substring(0, first) + text[first].uppercaseChar() + text.substring(first + 1)
    }
}
