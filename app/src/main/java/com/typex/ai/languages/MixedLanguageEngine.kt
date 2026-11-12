package com.typex.ai.languages

import com.typex.ai.languages.bengali.BengaliPhoneticEngine

/**
 * Explicit-mode mixed script helper. In Bengali mode, known English words
 * remain English; known Bengali phonetic words convert to Bengali.
 */
object MixedLanguageEngine {
    private val bengaliDictionary by lazy { BengaliPhoneticEngine.dictionaryEntries() }
    private val commonEnglish = setOf(
        "hello", "hi", "okay", "ok", "thanks", "thank", "please", "sorry",
        "good", "morning", "night", "yes", "no", "meeting", "email", "phone",
        "keyboard", "internet", "online", "offline", "app", "android", "typex",
        "ai", "chat", "message", "love", "work", "today", "tomorrow", "update",
        "project", "file", "download", "upload", "password", "login", "account",
        "support", "help", "test", "done", "continue", "phase"
    )

    fun resolveBengaliModeToken(
        roman: String,
        customBengali: Map<String, String> = emptyMap(),
        knownEnglishWords: Set<String> = emptySet()
    ): String {
        val token = roman.trim().lowercase()
        if (token.isEmpty()) return ""
        customBengali[token]?.let { return it }
        bengaliDictionary[token]?.let { return it }
        if (token in commonEnglish || token in knownEnglishWords) return roman
        return BengaliPhoneticEngine.transliterate(roman)
    }

    fun isKnownEnglishWord(token: String): Boolean =
        token.trim().lowercase() in commonEnglish
}
