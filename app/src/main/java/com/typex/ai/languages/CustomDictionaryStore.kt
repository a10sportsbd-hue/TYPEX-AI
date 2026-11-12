package com.typex.ai.languages

import android.content.Context

class CustomDictionaryStore(context: Context) {
    private val prefs = context.getSharedPreferences("typex_custom_dictionary", Context.MODE_PRIVATE)

    fun words(): Set<String> = prefs.getStringSet(KEY_WORDS, emptySet())?.toSet() ?: emptySet()

    fun add(word: String) {
        val normalized = word.trim()
        if (normalized.isBlank()) return
        prefs.edit().putStringSet(KEY_WORDS, words() + normalized).apply()
    }

    fun remove(word: String) {
        prefs.edit().putStringSet(KEY_WORDS, words() - word.trim()).apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_WORDS).apply()
    }

    companion object {
        private const val KEY_WORDS = "words"
    }
}
