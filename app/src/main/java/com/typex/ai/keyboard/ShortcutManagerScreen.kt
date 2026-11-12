package com.typex.ai.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShortcutManagerScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var trigger by remember { mutableStateOf("") }
    var expansion by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf(TextShortcutStore.all(context)) }
    Column(
        Modifier.fillMaxSize().background(Color(0xFF0B0B12)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("‹  Text Shortcuts", color = Color(0xFFB9A4FF), fontSize = 24.sp,
            modifier = Modifier.clickable { onBack() })
        Text("Create local shortcuts. Example: addr → your saved address. Use a letters-only trigger for now.",
            color = Color(0xFFB8B6C7), fontSize = 12.sp)
        OutlinedTextField(trigger, { trigger = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Trigger (e.g. ;sig)") }, singleLine = true)
        OutlinedTextField(expansion, { expansion = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Expansion text") }, minLines = 2)
        Button(onClick = {
            if (trigger.isNotBlank() && expansion.isNotBlank() &&
                trigger.none { it.isWhitespace() }) {
                TextShortcutStore.put(context, trigger, expansion)
                entries = TextShortcutStore.all(context)
                trigger = ""; expansion = ""
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Save shortcut") }
        Text("SAVED SHORTCUTS", color = Color(0xFF8E8AA4), fontSize = 11.sp)
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries.toList(), key = { it.first }) { item ->
                Row(Modifier.fillMaxWidth().background(Color(0xFF191827), RoundedCornerShape(12.dp)).padding(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(item.first, color = Color(0xFFB9A4FF))
                        Text(item.second, color = Color.White, fontSize = 12.sp)
                    }
                    Text("Delete", color = Color(0xFFFF8A8A),
                        modifier = Modifier.clickable {
                            TextShortcutStore.remove(context, item.first)
                            entries = TextShortcutStore.all(context)
                        }.padding(8.dp))
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
