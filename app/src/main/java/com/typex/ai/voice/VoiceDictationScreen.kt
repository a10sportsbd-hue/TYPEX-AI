package com.typex.ai.voice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import java.util.Locale

@Composable
fun VoiceDictationScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var transcript by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Tap the microphone to start dictation.") }
    var launchSpeechNow by remember { mutableStateOf<(() -> Unit)?>(null) }
    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        if (result.resultCode == Activity.RESULT_OK && !spoken.isNullOrBlank()) {
            transcript = if (transcript.isBlank()) spoken else "$transcript $spoken"
            status = "Dictation received."
        } else status = "No speech result received."
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchSpeechNow?.invoke()
        else status = "Microphone permission was denied."
    }
    launchSpeechNow = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        try { speechLauncher.launch(intent) }
        catch (_: Exception) { status = "Speech recognition is not available on this device." }
    }
    fun startDictation() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
        if (granted) launchSpeechNow?.invoke()
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFF0B0B12)).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("‹  Voice Typing", color = Color(0xFFB9A4FF), fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 8.dp))
        Text("Speech recognition is started only when you tap the button. Language availability depends on the device speech service.",
            color = Color(0xFFB8B6C7), fontSize = 13.sp)
        Button(onClick = { startDictation() }, modifier = Modifier.fillMaxWidth()) {
            Text("🎙  Start voice typing")
        }
        Text(status, color = Color(0xFFB9A4FF), fontSize = 13.sp)
        OutlinedTextField(
            value = transcript, onValueChange = { transcript = it },
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = { Text("Recognized text") },
            minLines = 5
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { transcript = "" }, modifier = Modifier.weight(1f)) {
                Text("Clear")
            }
            Button(onClick = {
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("TYPEX voice text", transcript))
                status = "Copied. Paste it into your text field."
            }, enabled = transcript.isNotBlank(), modifier = Modifier.weight(1f)) {
                Text("Copy text")
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

