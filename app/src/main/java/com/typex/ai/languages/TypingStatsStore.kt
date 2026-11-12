package com.typex.ai.languages

import android.content.Context

/** Aggregate counters only. No key values, words, sentences or app content are stored. */
object TypingStatsStore {
    private const val PREFS = "typex_typing_stats"
    private const val ENABLED = "stats_enabled"
    private const val KEYS = "key_count"
    private const val WORDS = "word_count"
    private const val SUGGESTIONS = "suggestion_count"

    private var pendingKeys = 0L
    private var pendingWords = 0L
    private var pendingSuggestions = 0L
    @Volatile private var cachedEnabled: Boolean? = null

    @Synchronized fun enabled(context: Context): Boolean {
        cachedEnabled?.let { return it }
        return prefs(context).getBoolean(ENABLED, false).also { cachedEnabled = it }
    }

    @Synchronized fun setEnabled(context: Context, value: Boolean) {
        cachedEnabled = value
        prefs(context).edit().putBoolean(ENABLED, value).apply()
        if (!value) flush(context)
    }

    @Synchronized fun recordKey(context: Context) {
        if (!enabled(context)) return
        pendingKeys++
        if (pendingKeys >= 25) flush(context)
    }

    @Synchronized fun recordWord(context: Context) {
        if (!enabled(context)) return
        pendingWords++
        if (pendingWords >= 10) flush(context)
    }

    @Synchronized fun recordSuggestion(context: Context) {
        if (!enabled(context)) return
        pendingSuggestions++
        if (pendingSuggestions >= 10) flush(context)
    }

    @Synchronized fun flush(context: Context) {
        if (pendingKeys == 0L && pendingWords == 0L && pendingSuggestions == 0L) return
        val p = prefs(context)
        p.edit()
            .putLong(KEYS, p.getLong(KEYS, 0) + pendingKeys)
            .putLong(WORDS, p.getLong(WORDS, 0) + pendingWords)
            .putLong(SUGGESTIONS, p.getLong(SUGGESTIONS, 0) + pendingSuggestions)
            .apply()
        pendingKeys = 0
        pendingWords = 0
        pendingSuggestions = 0
    }

    @Synchronized fun snapshot(context: Context): Map<String, Long> {
        val p = prefs(context)
        return mapOf(
            "keys" to p.getLong(KEYS, 0) + pendingKeys,
            "words" to p.getLong(WORDS, 0) + pendingWords,
            "suggestions" to p.getLong(SUGGESTIONS, 0) + pendingSuggestions
        )
    }

    @Synchronized fun clear(context: Context) {
        pendingKeys = 0
        pendingWords = 0
        pendingSuggestions = 0
        val wasEnabled = enabled(context)
        prefs(context).edit().clear().putBoolean(ENABLED, wasEnabled).apply()
        cachedEnabled = wasEnabled
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
