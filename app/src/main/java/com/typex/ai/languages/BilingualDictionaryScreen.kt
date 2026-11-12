package com.typex.ai.languages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BilingualDictionaryScreen(onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { BilingualDictionaryEngine.search(query) }
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B12)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("‹  English ↔ বাংলা Dictionary", color = Color(0xFFB9A4FF), fontSize = 22.sp)
        OutlinedTextField(
            value = query, onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search English or বাংলা") }, singleLine = true
        )
        Text("${results.size} results", color = Color(0xFFB8B6C7), fontSize = 12.sp)
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(results) { entry ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF191827)),
                    modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(entry.english, color = Color(0xFFB9A4FF))
                            Text(entry.bengali, color = Color.White)
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
