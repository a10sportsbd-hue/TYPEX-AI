package com.typex.ai.ai

enum class AiAction(val label: String, val instruction: String) {
    GRAMMAR("Fix grammar", "Correct grammar and spelling. Preserve the original meaning. Return only the corrected text."),
    REWRITE("Rewrite", "Rewrite the text clearly and naturally while preserving its meaning. Return only the rewritten text."),
    PROFESSIONAL("Professional", "Rewrite the text in a concise, professional tone. Return only the result."),
    SHORTEN("Shorten", "Make the text shorter while preserving the key meaning. Return only the result."),
    EXPAND("Expand", "Expand the text with useful detail without inventing facts. Return only the result."),
    BN_TO_EN("Bangla to English", "Translate the text from Bangla to natural English. Return only the translation."),
    EN_TO_BN("English to Bangla", "Translate the text into natural Bangladeshi Bengali. Return only the translation."),
    SMART_REPLY("Smart reply", "Suggest one appropriate reply to this message. Do not invent personal facts. Return only the reply.")
}

data class AiRequest(
    val text: String,
    val action: AiAction,
    val sourceLanguage: String = "auto"
)

data class AiResult(
    val text: String,
    val provider: String,
    val isOffline: Boolean
)

sealed class AiFailure(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Configuration(message: String) : AiFailure(message)
    class Network(message: String, cause: Throwable? = null) : AiFailure(message, cause)
    class InvalidResponse(message: String) : AiFailure(message)
}
