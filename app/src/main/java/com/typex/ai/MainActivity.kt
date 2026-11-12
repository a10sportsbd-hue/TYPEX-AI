package com.typex.ai

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.typex.ai.ai.AiStudioScreen
import com.typex.ai.voice.VoiceDictationScreen
import com.typex.ai.keyboard.ShortcutManagerScreen
import com.typex.ai.languages.bengali.BengaliDictionaryScreen
import com.typex.ai.languages.PersonalLearningScreen
import com.typex.ai.languages.TypingStatsScreen
import com.typex.ai.languages.BilingualDictionaryScreen
import com.typex.ai.themes.KeyboardTheme
import com.typex.ai.themes.TypexThemes
import com.typex.ai.ui.theme.TypexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TypexTheme {
                Surface(Modifier.fillMaxSize(), color = Color(0xFF0B0B12)) {
                    TypexHome(
                        initialAiStudio = intent.getBooleanExtra("open_ai_studio", false),
                        initialVoice = intent.getBooleanExtra("open_voice_dictation", false),
                        onOpenImeSettings = { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) },
                        onOpenImePicker = {
                            val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                            imm.showInputMethodPicker()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TypexHome(
    initialAiStudio: Boolean = false,
    initialVoice: Boolean = false,
    onOpenImeSettings: () -> Unit,
    onOpenImePicker: () -> Unit
) {
    var showStudio by remember { mutableStateOf(false) }
    var showAiStudio by remember { mutableStateOf(initialAiStudio) }
    var showVoice by remember { mutableStateOf(initialVoice) }
    var showShortcuts by remember { mutableStateOf(false) }
    var showBengaliDictionary by remember { mutableStateOf(false) }
    var showPersonalLearning by remember { mutableStateOf(false) }
    var showTypingStats by remember { mutableStateOf(false) }
    var showBilingualDictionary by remember { mutableStateOf(false) }
    if (showTypingStats) { TypingStatsScreen(onBack = { showTypingStats = false }); return }
    if (showBilingualDictionary) { BilingualDictionaryScreen(onBack = { showBilingualDictionary = false }); return }
    if (showPersonalLearning) { PersonalLearningScreen(onBack = { showPersonalLearning = false }); return }
    if (showBengaliDictionary) { BengaliDictionaryScreen(onBack = { showBengaliDictionary = false }); return }
    if (showVoice) { VoiceDictationScreen(onBack = { showVoice = false }); return }
    if (showShortcuts) { ShortcutManagerScreen(onBack = { showShortcuts = false }); return }
    if (showStudio) {
        ThemeStudio(onBack = { showStudio = false })
        return
    }
    if (showAiStudio) {
        AiStudioScreen(onBack = { showAiStudio = false })
        return
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFF0B0B12))
            .verticalScroll(rememberScrollState()).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text("TYPEX AI", color = Color(0xFFB9A4FF), fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Text("Type beyond limits.", color = Color(0xFFB8B6C7), fontSize = 14.sp)

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF191827))
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Your keyboard", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Enable TYPEX AI and select it as your active keyboard.", color = Color(0xFFC7C4D6), fontSize = 13.sp)
                Spacer(Modifier.height(14.dp))
                Button(onClick = onOpenImeSettings, modifier = Modifier.fillMaxWidth()) {
                    Text("Enable TYPEX AI Keyboard")
                }
                Button(onClick = onOpenImePicker, modifier = Modifier.fillMaxWidth()) {
                    Text("Select Keyboard")
                }
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showBilingualDictionary = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2C3A))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("English ↔ বাংলা Dictionary", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Search common bilingual word meanings", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFF91B8FF), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showTypingStats = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF302B3C))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Typing Statistics", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Optional local aggregate counters", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFFD5B8FF), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showPersonalLearning = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B293A))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Personal Learning", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Opt-in vocabulary learning and privacy controls", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFFD5B8FF), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showAiStudio = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF17263A))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("AI Studio", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Grammar, rewrite and translation tools", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFF7CCBFF), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showVoice = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF18312D))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Voice Typing", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Tap-to-start system speech recognition", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFF8BE0C1), fontWeight = FontWeight.Bold)
            }
        }
        Card(
            Modifier.fillMaxWidth().clickable { showShortcuts = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF30253A))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Text Shortcuts", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Create local text expansions", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFFD5B8FF), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showBengaliDictionary = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF25302C))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Bengali Dictionary", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Manage your own Roman-to-Bangla words", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFF8BE0C1), fontWeight = FontWeight.Bold)
            }
        }

        Card(
            Modifier.fillMaxWidth().clickable { showStudio = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF211C35))
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Theme Studio", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Customize colors, key size, sound and vibration", color = Color(0xFFC7C4D6), fontSize = 12.sp)
                }
                Text("OPEN  →", color = Color(0xFFB9A4FF), fontWeight = FontWeight.Bold)
            }
        }

        Text("PROJECT STATUS", color = Color(0xFF8E8AA4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        StatusRow("Keyboard engine", "Phase 2")
        StatusRow("Bengali phonetic", "Phase 3")
        StatusRow("Language prediction foundation", "Phase 4")
        StatusRow("Theme Studio", "Phase 5")
        StatusRow("AI Studio", "Phase 6")
        StatusRow("Voice & shortcuts", "Phase 8")
        StatusRow("Bengali prediction", "Phase 12")
        Spacer(Modifier.weight(1f))
        Text("Privacy first • Local-first architecture", color = Color(0xFF777487), fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun ThemeStudio(onBack: () -> Unit) {
    val context = LocalContext.current
    var selectedId by remember { mutableStateOf(TypexThemes.selected(context).id) }
    var keyHeight by remember { mutableFloatStateOf(TypexThemes.keyHeight(context).toFloat()) }
    var sound by remember { mutableStateOf(TypexThemes.keySoundEnabled(context)) }
    var vibration by remember { mutableStateOf(TypexThemes.vibrationEnabled(context)) }
    var keySpacing by remember { mutableFloatStateOf(TypexThemes.keySpacing(context).toFloat()) }
    var toolbarEnabled by remember { mutableStateOf(TypexThemes.toolbarEnabled(context)) }
    var compactMode by remember { mutableStateOf(TypexThemes.compactMode(context)) }
    var oneHandWidth by remember { mutableFloatStateOf(TypexThemes.oneHandWidth(context).toFloat()) }
    var swipePreview by remember { mutableStateOf(TypexThemes.swipePreviewEnabled(context)) }
    var keyAnimations by remember { mutableStateOf(TypexThemes.keyAnimationsEnabled(context)) }
    var keyboardLayout by remember { mutableStateOf(TypexThemes.keyboardLayout(context)) }
    var cornerRadius by remember { mutableFloatStateOf(TypexThemes.keyCornerRadius(context).toFloat()) }
    var keyTextSize by remember { mutableFloatStateOf(TypexThemes.keyTextSize(context).toFloat()) }
    var sideInset by remember { mutableFloatStateOf(TypexThemes.keyboardSideInset(context).toFloat()) }
    val selected = TypexThemes.get(selectedId)

    Column(
        Modifier.fillMaxSize().background(Color(0xFF0B0B12)).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("‹", color = Color(0xFFB9A4FF), fontSize = 32.sp, modifier = Modifier.clickable { onBack() }.padding(end = 12.dp))
            Column {
                Text("Theme Studio", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                Text("Make your keyboard yours", color = Color(0xFFAAA6BB), fontSize = 12.sp)
            }
        }

        Text("LIVE PREVIEW", color = Color(0xFF8E8AA4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        KeyboardPreview(selected, keyHeight.toInt())

        Text("CHOOSE A THEME", color = Color(0xFF8E8AA4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().height(176.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(TypexThemes.all) { theme ->
                ThemeCard(theme, selectedId == theme.id) {
                    selectedId = theme.id
                    TypexThemes.save(context, theme.id)
                }
            }
        }

        Text("KEY HEIGHT  ${keyHeight.toInt()} dp", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Slider(
            value = keyHeight,
            onValueChange = { keyHeight = it },
            valueRange = 38f..60f,
            onValueChangeFinished = { TypexThemes.saveKeyHeight(context, keyHeight.toInt()) }
        )
        SettingToggle("Key sound", sound) {
            sound = it
            TypexThemes.setKeySound(context, it)
        }
        SettingToggle("Vibration", vibration) {
            vibration = it
            TypexThemes.setVibration(context, it)
        }

        Text("ADVANCED KEYBOARD CONTROLS", color = Color(0xFF8E8AA4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text("KEY SPACING  ${keySpacing.toInt()} dp", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Slider(value = keySpacing, onValueChange = { keySpacing = it }, valueRange = 0f..6f, steps = 5,
            onValueChangeFinished = { TypexThemes.saveKeySpacing(context, keySpacing.toInt()) })
        SettingToggle("Compact keyboard", compactMode) {
            compactMode = it
            TypexThemes.setCompactMode(context, it)
        }
        SettingToggle("Show keyboard toolbar", toolbarEnabled) {
            toolbarEnabled = it
            TypexThemes.setToolbarEnabled(context, it)
        }
        SettingToggle("Show swipe path preview", swipePreview) {
            swipePreview = it
            TypexThemes.setSwipePreviewEnabled(context, it)
        }
        SettingToggle("Key press animations", keyAnimations) {
            keyAnimations = it
            TypexThemes.setKeyAnimationsEnabled(context, it)
        }

        Text("KEYBOARD LAYOUT", color = Color(0xFF8E8AA4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            listOf("qwerty", "azerty", "qwertz").forEach { layout ->
                Button(
                    onClick = { keyboardLayout = layout; TypexThemes.saveKeyboardLayout(context, layout) },
                    modifier = Modifier.weight(1f)
                ) { Text(layout.uppercase(), fontSize = 10.sp) }
            }
        }
        Text("KEY CORNER RADIUS  ${cornerRadius.toInt()} dp", color = Color.White, fontSize = 13.sp)
        Slider(value = cornerRadius, onValueChange = { cornerRadius = it },
            valueRange = 4f..18f, onValueChangeFinished = { TypexThemes.saveKeyCornerRadius(context, cornerRadius.toInt()) })
        Text("KEY TEXT SIZE  ${keyTextSize.toInt()} sp", color = Color.White, fontSize = 13.sp)
        Slider(value = keyTextSize, onValueChange = { keyTextSize = it },
            valueRange = 16f..24f, steps = 7, onValueChangeFinished = { TypexThemes.saveKeyTextSize(context, keyTextSize.toInt()) })
        Text("SIDE INSET  ${sideInset.toInt()} dp", color = Color.White, fontSize = 13.sp)
        Slider(value = sideInset, onValueChange = { sideInset = it },
            valueRange = 0f..14f, steps = 13, onValueChangeFinished = { TypexThemes.saveKeyboardSideInset(context, sideInset.toInt()) })

        Text("ONE-HAND WIDTH  ${oneHandWidth.toInt()} dp", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Slider(value = oneHandWidth, onValueChange = { oneHandWidth = it }, valueRange = 250f..350f, steps = 9,
            onValueChangeFinished = { TypexThemes.saveOneHandWidth(context, oneHandWidth.toInt()) })
        Text("Changes apply when TYPEX AI keyboard is opened again. One-hand width is used when the ↔ toolbar control is active.",
            color = Color(0xFFAAA6BB), fontSize = 11.sp)
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Done") }
    }
}

@Composable
private fun KeyboardPreview(theme: KeyboardTheme, keyHeight: Int) {
    Column(
        Modifier.fillMaxWidth().background(Color(theme.background), RoundedCornerShape(18.dp)).padding(9.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        listOf("q w e r t y u i o p", "a s d f g h j k l", "⇧ z x c v b n m ⌫").forEach { line ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                line.split(" ").forEach { key ->
                    Text(
                        key,
                        modifier = Modifier.weight(1f).height((keyHeight * 0.55f).dp)
                            .background(Color(theme.key), RoundedCornerShape(theme.cornerRadiusDp.dp)),
                        color = Color(theme.text),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("?123", "🌐", "space", ".", "↵").forEach { key ->
                Text(
                    key,
                    modifier = Modifier.weight(if (key == "space") 3f else 1f)
                        .height(27.dp).background(Color(theme.specialKey), RoundedCornerShape(7.dp)),
                    color = Color(theme.text),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(theme: KeyboardTheme, selected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth()
            .border(if (selected) 2.dp else 1.dp, if (selected) Color(0xFFB9A4FF) else Color(0xFF353244), RoundedCornerShape(14.dp))
            .background(Color(0xFF191827), RoundedCornerShape(14.dp))
            .clickable { onClick() }.padding(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(3) {
                Spacer(Modifier.size(23.dp).background(Color(theme.key), RoundedCornerShape(6.dp)))
            }
            Spacer(Modifier.size(23.dp).background(Color(theme.specialKey), RoundedCornerShape(6.dp)))
        }
        Spacer(Modifier.height(8.dp))
        Text(theme.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        if (selected) Text("SELECTED", color = Color(0xFFB9A4FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingToggle(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.White, modifier = Modifier.weight(1f), fontSize = 13.sp)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun StatusRow(label: String, status: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color(0xFFE8E5F2), fontSize = 13.sp)
        Text(status, color = Color(0xFFB9A4FF), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
