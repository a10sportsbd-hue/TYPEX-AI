package com.typex.ai.languages.bengali

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
fun BengaliDictionaryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var roman by remember { mutableStateOf("") }
    var bangla by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf(BengaliCustomDictionaryStore.all(context)) }
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B12)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("‹  Bengali Dictionary", color = Color(0xFFB9A4FF), fontSize = 23.sp,
            modifier = Modifier.clickable { onBack() })
        Text("Add your own Roman-to-Bengali word mappings. These stay on this device.",
            color = Color(0xFFB8B6C7), fontSize = 12.sp)
        OutlinedTextField(roman, { roman = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Roman spelling (e.g. amar)") }, singleLine = true)
        OutlinedTextField(bangla, { bangla = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("বাংলা শব্দ") }, singleLine = true)
        Button(onClick = {
            if (roman.isNotBlank() && bangla.isNotBlank() && roman.none { it.isWhitespace() }) {
                BengaliCustomDictionaryStore.put(context, roman, bangla)
                entries = BengaliCustomDictionaryStore.all(context)
                roman = ""; bangla = ""
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Save Bengali word") }
        Text("MY WORDS (${entries.size})", color = Color(0xFF8E8AA4), fontSize = 11.sp)
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(entries.toList(), key = { it.first }) { entry ->
                Row(Modifier.fillMaxWidth().background(Color(0xFF191827), RoundedCornerShape(12.dp)).padding(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(entry.key, color = Color(0xFFB9A4FF))
                        Text(entry.value, color = Color.White)
                    }
                    Text("Delete", color = Color(0xFFFF8A8A),
                        modifier = Modifier.clickable {
                            BengaliCustomDictionaryStore.remove(context, entry.key)
                            entries = BengaliCustomDictionaryStore.all(context)
                        }.padding(8.dp))
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
