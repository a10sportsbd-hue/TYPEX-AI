package com.typex.ai.languages

/**
 * Small offline next-word phrase hints. These are suggestions, never automatic
 * insertions, and do not inspect text in the target application.
 */
object SentencePredictionEngine {
    private val english = mapOf(
        "good" to listOf("morning", "evening", "night"),
        "how" to listOf("are", "is", "can"),
        "thank" to listOf("you"),
        "see" to listOf("you", "you soon"),
        "i" to listOf("am", "will", "can", "need", "want"),
        "please" to listOf("help", "send", "check"),
        "let" to listOf("me", "us"),
        "are" to listOf("you", "we", "they")
    )
    private val bengali = mapOf(
        "আমি" to listOf("ভালো", "আছি", "যাব", "চাই", "করব"),
        "তুমি" to listOf("কেমন", "কোথায়", "কি", "আছো"),
        "আপনি" to listOf("কেমন", "কোথায়", "আছেন"),
        "ধন্যবাদ" to listOf("আপনাকে", "অনেক"),
        "বাংলাদেশ" to listOf("আমার", "একটি", "সুন্দর"),
        "ভালো" to listOf("আছি", "লাগে", "বাসি")
    )

    fun nextWords(previousWord: String, bengaliMode: Boolean, limit: Int = 4): List<String> {
        val key = previousWord.trim().lowercase()
        if (key.isEmpty()) return emptyList()
        val source = if (bengaliMode) bengali else english
        return source[key].orEmpty().take(limit)
    }
}
