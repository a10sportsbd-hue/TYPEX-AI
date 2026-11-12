package com.typex.ai.keyboard

import com.typex.ai.languages.MixedLanguageEngine

/**
 * Owns the active Roman input composition for Bengali phonetic typing.
 * Preview, commit and deletion are explicit operations to keep IME state consistent.
 */
class BengaliCompositionController {
    private val buffer = StringBuilder()

    fun append(value: String, customWords: Map<String, String> = emptyMap(), knownEnglishWords: Set<String> = emptySet()): String {
        if (value.isNotEmpty()) buffer.append(value)
        return preview(customWords, knownEnglishWords)
    }

    fun preview(customWords: Map<String, String> = emptyMap(), knownEnglishWords: Set<String> = emptySet()): String =
        MixedLanguageEngine.resolveBengaliModeToken(buffer.toString(), customWords, knownEnglishWords)

    fun commit(customWords: Map<String, String> = emptyMap(), knownEnglishWords: Set<String> = emptySet()): String {
        val result = preview(customWords, knownEnglishWords)
        buffer.clear()
        return result
    }

    fun backspace(): Boolean {
        if (buffer.isEmpty()) return false
        buffer.deleteCharAt(buffer.length - 1)
        return true
    }

    fun raw(): String = buffer.toString()
    fun isEmpty(): Boolean = buffer.isEmpty()
    fun clear() = buffer.clear()
}