package com.typex.ai.languages

/**
 * Small phrase-completion catalog. Results are optional chips, never inserted
 * automatically and do not inspect text outside TYPEX's own tracked input.
 */
object PhraseCompletionEngine {
    private val english = mapOf(
        "hello" to listOf("Hello, how are you?", "Hello, nice to meet you."),
        "good" to listOf("Good morning!", "Good evening!", "Good night!"),
        "thank" to listOf("Thank you so much.", "Thank you for your help."),
        "how" to listOf("How are you?", "How can I help you?"),
        "i" to listOf("I am doing well.", "I will let you know."),
        "please" to listOf("Please let me know.", "Please check and confirm."),
        "let" to listOf("Let me know.", "Let's talk later."),
        "see" to listOf("See you soon.", "See you tomorrow.")
    )
    private val bengali = mapOf(
        "আমি" to listOf("আমি ভালো আছি।", "আমি পরে জানাব।"),
        "তুমি" to listOf("তুমি কেমন আছো?", "তুমি কি আসবে?"),
        "আপনি" to listOf("আপনি কেমন আছেন?", "আপনাকে ধন্যবাদ।"),
        "ধন্যবাদ" to listOf("ধন্যবাদ আপনাকে।", "অনেক ধন্যবাদ।"),
        "ভালো" to listOf("ভালো আছি।", "ভালো লাগলো।"),
        "বাংলাদেশ" to listOf("বাংলাদেশ আমার দেশ।", "বাংলাদেশ অনেক সুন্দর।")
    )

    fun completions(contextWord: String, bengaliMode: Boolean, limit: Int = 3): List<String> {
        val key = contextWord.trim().lowercase()
        if (key.isEmpty()) return emptyList()
        val source = if (bengaliMode) bengali else english
        return source[key].orEmpty().take(limit)
    }
}
