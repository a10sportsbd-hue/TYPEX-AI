package com.typex.ai.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AiStudioScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    var endpoint by remember { mutableStateOf("https://api.openai.com/v1") }
    var model by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var useOnline by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf(AiAction.GRAMMAR) }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var providerLabel by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxSize().background(Color(0xFF0B0B12)).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹ Back") }
        Text("AI Studio", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("Use AI only when you choose an action and submit selected text.", color = Color(0xFFB8B6C7), fontSize = 12.sp)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { useOnline = false; error = "" }) { Text("Offline") }
            Button(onClick = { useOnline = true; error = "" }) { Text("Online") }
        }

        if (useOnline) {
            Text("OpenAI-compatible API settings", color = Color(0xFFB9A4FF), fontWeight = FontWeight.SemiBold)
            OutlinedTextField(endpoint, { endpoint = it }, label = { Text("API base URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(model, { model = it }, label = { Text("Model ID") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(apiKey, { apiKey = it }, label = { Text("API key (kept in memory only)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Text("For production, use a trusted backend proxy. Never embed a private API key in a public APK.", color = Color(0xFFFFC98B), fontSize = 11.sp)
        } else {
            Text("Offline mode uses local phrase lookup and basic text utilities. It is not a downloaded generative AI model.", color = Color(0xFFB8B6C7), fontSize = 12.sp)
        }

        Text("Choose an action", color = Color.White, fontWeight = FontWeight.SemiBold)
        AiAction.values().toList().chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pair.forEach { action ->
                    Button(
                        onClick = { selectedAction = action },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (selectedAction == action) "✓ ${action.label}" else action.label, fontSize = 11.sp)
                    }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter text to process") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Button(
            onClick = {
                error = ""
                output = ""
                busy = true
                scope.launch {
                    try {
                        val provider: AiProvider = if (useOnline) {
                            OnlineAiProvider(endpoint, apiKey, model)
                        } else {
                            OfflineAssistProvider()
                        }
                        val result = provider.process(AiRequest(input, selectedAction))
                        output = result.text
                        providerLabel = result.provider
                    } catch (e: Exception) {
                        error = e.message ?: "Something went wrong."
                    } finally {
                        busy = false
                    }
                }
            },
            enabled = !busy && input.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (busy) CircularProgressIndicator() else Text("Run ${selectedAction.label}")
        }

        if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A8A), fontSize = 12.sp)
        if (output.isNotBlank()) {
            Text("RESULT • $providerLabel", color = Color(0xFFB9A4FF), fontWeight = FontWeight.Bold)
            Text(output, color = Color.White, fontSize = 15.sp)
            TextButton(onClick = { input = output; output = "" }) { Text("Use result as input") }
        }
        Spacer(Modifier.height(20.dp))
    }
}
