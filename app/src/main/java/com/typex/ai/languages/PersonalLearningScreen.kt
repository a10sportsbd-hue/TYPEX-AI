package com.typex.ai.languages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val learningBg = Color(0xFF0B0B12)
private val learningCard = Color(0xFF191827)
private val learningAccent = Color(0xFFB9A4FF)

@Composable
fun PersonalLearningScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var englishEnabled by remember { mutableStateOf(UserVocabularyStore.isEnglishEnabled(context)) }
    var bengaliEnabled by remember { mutableStateOf(UserVocabularyStore.isBengaliEnabled(context)) }
    var englishWords by remember { mutableStateOf(UserVocabularyStore.ranked(context)) }
    var bengaliWords by remember { mutableStateOf(UserVocabularyStore.bengaliScores(context).entries.sortedByDescending { it.value }) }
    var clearTarget by remember { mutableStateOf<String?>(null) }

    fun refresh() {
        englishWords = UserVocabularyStore.ranked(context)
        bengaliWords = UserVocabularyStore.bengaliScores(context).entries.sortedByDescending { it.value }
    }

    Column(Modifier.fillMaxSize().background(learningBg).padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("‹", color = learningAccent, fontSize = 32.sp,
                modifier = Modifier.clickable { onBack() }.padding(end = 12.dp))
            Column {
                Text("Personal Learning", color = Color.White, fontSize = 23.sp)
                Text("Your words stay on this device", color = Color(0xFFAAA6BB), fontSize = 12.sp)
            }
        }
        Text("TYPEX only learns from suggestions you explicitly accept. It does not collect surrounding sentences or send your vocabulary to a server.",
            color = Color(0xFFB8B6C7), fontSize = 12.sp)
        LearningSwitch("English learning", "Personal English words and ranking", englishEnabled) {
            englishEnabled = it; UserVocabularyStore.setEnglishEnabled(context, it)
        }
        LearningSwitch("বাংলা learning", "বাংলা suggestion ranking", bengaliEnabled) {
            bengaliEnabled = it; UserVocabularyStore.setBengaliEnabled(context, it)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ENGLISH WORDS", color = Color(0xFF8E8AA4), fontSize = 11.sp)
            Text("${englishWords.size}", color = learningAccent, fontSize = 11.sp)
        }
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(englishWords, key = { "en_$it" }) { word ->
                LearningWordRow(word, UserVocabularyStore.scores(context)[word] ?: 0, "Remove") {
                    UserVocabularyStore.remove(context, word); refresh()
                }
            }
            item {
                Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("বাংলা WORD COUNTS", color = Color(0xFF8E8AA4), fontSize = 11.sp)
                    Text("${bengaliWords.size}", color = learningAccent, fontSize = 11.sp)
                }
            }
            items(bengaliWords, key = { "bn_${it.key}" }) { entry ->
                LearningWordRow(entry.key, entry.value, "Remove") {
                    UserVocabularyStore.removeBengali(context, entry.key); refresh()
                }
            }
            if (englishWords.isEmpty() && bengaliWords.isEmpty()) {
                item { Text("No learned words yet. Enable a language and accept suggestions to build your dictionary.",
                    color = Color(0xFFAAA6BB), fontSize = 13.sp, modifier = Modifier.padding(vertical = 20.dp)) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { clearTarget = "english" }, modifier = Modifier.weight(1f)) { Text("Reset English") }
            OutlinedButton(onClick = { clearTarget = "bengali" }, modifier = Modifier.weight(1f)) { Text("Reset বাংলা") }
        }
        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.End)) { Text("Done") }
    }

    if (clearTarget != null) {
        val target = clearTarget!!
        AlertDialog(
            onDismissRequest = { clearTarget = null },
            title = { Text("Reset learned words?") },
            text = { Text(if (target == "english") "All learned English words and their counts will be deleted." else "All learned Bengali word counts will be deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    if (target == "english") UserVocabularyStore.clearEnglish(context) else UserVocabularyStore.clearBengali(context)
                    refresh(); clearTarget = null
                }) { Text("Reset") }
            },
            dismissButton = { TextButton(onClick = { clearTarget = null }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun LearningSwitch(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().background(learningCard, RoundedCornerShape(14.dp)).padding(13.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp)
            Text(subtitle, color = Color(0xFFAAA6BB), fontSize = 11.sp)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun LearningWordRow(word: String, count: Int, action: String, onRemove: () -> Unit) {
    Row(Modifier.fillMaxWidth().background(learningCard, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(word, color = Color.White, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text("×$count", color = Color(0xFFAAA6BB), fontSize = 11.sp)
        TextButton(onClick = onRemove) { Text(action, color = Color(0xFFFF8A8A), fontSize = 11.sp) }
    }
}
