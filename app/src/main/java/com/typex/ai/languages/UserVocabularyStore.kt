package com.typex.ai.languages

import android.content.Context

/**
 * Explicit, on-device word learning. English and Bengali switches are independent.
 * Existing installations inherit the previous shared learning_enabled preference.
 */
object UserVocabularyStore {
    private const val PREFS = "typex_user_vocabulary"
    private const val ENABLED = "learning_enabled"
    private const val ENGLISH_ENABLED = "learning_english_enabled"
    private const val BENGALI_ENABLED = "learning_bengali_enabled"
    private const val WORDS = "accepted_words"
    private const val COUNT_PREFIX = "accepted_count_"
    private const val BENGALI_COUNT_PREFIX = "accepted_bn_count_"
    private const val MAX_WORDS = 1000
    private const val MAX_ACCEPT_COUNT = 10_000

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isEnabled(context: Context): Boolean = isEnglishEnabled(context) || isBengaliEnabled(context)

    fun setEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(ENABLED, enabled)
            .putBoolean(ENGLISH_ENABLED, enabled).putBoolean(BENGALI_ENABLED, enabled).apply()
    }

    fun isEnglishEnabled(context: Context): Boolean =
        prefs(context).let { if (it.contains(ENGLISH_ENABLED)) it.getBoolean(ENGLISH_ENABLED, false)
            else it.getBoolean(ENABLED, false) }

    fun isBengaliEnabled(context: Context): Boolean =
        prefs(context).let { if (it.contains(BENGALI_ENABLED)) it.getBoolean(BENGALI_ENABLED, false)
            else it.getBoolean(ENABLED, false) }

    fun setEnglishEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(ENGLISH_ENABLED, enabled).apply()
    }

    fun setBengaliEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(BENGALI_ENABLED, enabled).apply()
    }

    fun all(context: Context): Set<String> = prefs(context).getStringSet(WORDS, emptySet()).orEmpty().toSet()

    fun add(context: Context, word: String) {
        val clean = word.trim().lowercase()
        if (clean.isBlank() || clean.length > 40 || !isEnglishEnabled(context)) return
        prefs(context).edit().putStringSet(WORDS, (all(context) + clean).take(MAX_WORDS).toSet()).apply()
    }

    fun recordAccepted(context: Context, word: String) {
        val clean = word.trim().lowercase()
        if (clean.isBlank() || clean.length > 40 || !isEnglishEnabled(context)) return
        val p = prefs(context)
        val count = p.getInt(COUNT_PREFIX + clean, 0).coerceAtMost(MAX_ACCEPT_COUNT)
        p.edit().putStringSet(WORDS, (all(context) + clean).take(MAX_WORDS).toSet())
            .putInt(COUNT_PREFIX + clean, (count + 1).coerceAtMost(MAX_ACCEPT_COUNT)).apply()
    }

    fun recordAcceptedBengali(context: Context, word: String) {
        val clean = word.trim()
        if (clean.isBlank() || clean.length > 40 || !isBengaliEnabled(context)) return
        val p = prefs(context)
        val key = BENGALI_COUNT_PREFIX + clean
        p.edit().putInt(key, (p.getInt(key, 0).coerceAtMost(MAX_ACCEPT_COUNT) + 1).coerceAtMost(MAX_ACCEPT_COUNT)).apply()
    }

    fun bengaliScores(context: Context): Map<String, Int> = prefs(context).all.mapNotNull { (key, value) ->
        if (key.startsWith(BENGALI_COUNT_PREFIX) && value is Int) key.removePrefix(BENGALI_COUNT_PREFIX) to value else null
    }.toMap()

    fun scores(context: Context): Map<String, Int> = all(context).associateWith { prefs(context).getInt(COUNT_PREFIX + it, 0) }

    fun ranked(context: Context): List<String> = scores(context).entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key }).map { it.key }

    fun remove(context: Context, word: String) {
        val clean = word.trim().lowercase()
        prefs(context).edit().putStringSet(WORDS, all(context) - clean).remove(COUNT_PREFIX + clean).apply()
    }

    fun removeBengali(context: Context, word: String) {
        prefs(context).edit().remove(BENGALI_COUNT_PREFIX + word.trim()).apply()
    }

    fun clearEnglish(context: Context) {
        val p = prefs(context); val edit = p.edit().remove(WORDS)
        p.all.keys.filter { it.startsWith(COUNT_PREFIX) }.forEach(edit::remove); edit.apply()
    }

    fun clearBengali(context: Context) {
        val p = prefs(context); val edit = p.edit()
        p.all.keys.filter { it.startsWith(BENGALI_COUNT_PREFIX) }.forEach(edit::remove); edit.apply()
    }

    fun clear(context: Context) { clearEnglish(context); clearBengali(context) }
}
