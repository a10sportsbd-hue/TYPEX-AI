package com.typex.ai.languages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TypingStatsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var enabled by remember { mutableStateOf(TypingStatsStore.enabled(context)) }
    var values by remember { mutableStateOf(TypingStatsStore.snapshot(context)) }
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B12)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("‹  Typing Statistics", color = Color(0xFFB9A4FF), fontSize = 23.sp)
        Text("Only aggregate counters are stored. TYPEX does not save typed words, messages or app content.",
            color = Color(0xFFB8B6C7), fontSize = 13.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text("Enable typing statistics", color = Color.White)
                Text(if (enabled) "Counting is on" else "Counting is off", color = Color(0xFFB8B6C7), fontSize = 12.sp)
            }
            Switch(checked = enabled, onCheckedChange = {
                enabled = it
                TypingStatsStore.setEnabled(context, it)
            })
        }
        StatTile("Letter & space key presses", values["keys"] ?: 0)
        StatTile("Word boundaries", values["words"] ?: 0)
        StatTile("Accepted suggestions", values["suggestions"] ?: 0)
        OutlinedButton(onClick = {
            TypingStatsStore.clear(context)
            values = TypingStatsStore.snapshot(context)
        }, modifier = Modifier.fillMaxWidth()) { Text("Reset statistics") }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

@Composable
private fun StatTile(label: String, value: Long) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF191827)),
        modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color(0xFFB8B6C7), fontSize = 12.sp)
            Text(value.toString(), color = Color.White, fontSize = 25.sp)
        }
    }
}
