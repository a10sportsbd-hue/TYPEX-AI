package com.typex.ai.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Offline fallback utilities. This is NOT an on-device LLM.
 * It performs limited deterministic transformations and phrase lookup only.
 */
class OfflineAssistProvider : AiProvider {
    private val bnToEn = mapOf(
        "আমি" to "I", "তুমি" to "you", "আপনি" to "you",
        "ভালো" to "good", "কেমন আছো" to "how are you",
        "ধন্যবাদ" to "thank you", "বাংলা" to "Bangla",
        "বাংলাদেশ" to "Bangladesh", "হ্যাঁ" to "yes", "না" to "no"
    )
    private val enToBn = mapOf(
        "i" to "আমি", "you" to "তুমি", "good" to "ভালো",
        "hello" to "হ্যালো", "how are you" to "কেমন আছো",
        "thank you" to "ধন্যবাদ", "bangladesh" to "বাংলাদেশ",
        "yes" to "হ্যাঁ", "no" to "না"
    )

    override suspend fun process(request: AiRequest): AiResult = withContext(Dispatchers.Default) {
        val input = request.text.trim()
        if (input.isEmpty()) throw AiFailure.Configuration("Enter some text first.")

        val output = when (request.action) {
            AiAction.GRAMMAR -> basicGrammar(input)
            AiAction.REWRITE -> basicGrammar(input)
            AiAction.PROFESSIONAL -> basicGrammar(input)
            AiAction.SHORTEN -> input.split(Regex("\\s+")).take(12).joinToString(" ")
            AiAction.EXPAND -> input
            AiAction.BN_TO_EN -> translatePhrases(input, bnToEn)
            AiAction.EN_TO_BN -> translatePhrases(input.lowercase(), enToBn)
            AiAction.SMART_REPLY -> "Thanks for your message."
        }

        AiResult(
            text = output,
            provider = "Offline basic tools",
            isOffline = true
        )
    }

    private fun basicGrammar(text: String): String {
        val normalized = text.replace(Regex("\\s+"), " ").trim()
        if (normalized.isEmpty()) return normalized
        val first = normalized.substring(0, 1).uppercase()
        val rest = normalized.substring(1)
        val punctuated = if (normalized.last() in listOf('.', '!', '?', '।')) normalized else "$normalized."
        return first + punctuated.substring(1)
    }

    private fun translatePhrases(text: String, dictionary: Map<String, String>): String {
        dictionary[text]?.let { return it }
        var result = text
        dictionary.entries.sortedByDescending { it.key.length }.forEach { (from, to) ->
            result = result.replace(from, to, ignoreCase = true)
        }
        return result
    }
}
