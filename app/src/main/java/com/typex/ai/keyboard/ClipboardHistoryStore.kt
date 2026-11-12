package com.typex.ai.keyboard

import android.content.Context

object ClipboardHistoryStore {
    private const val PREFS = "typex_clipboard_history"
    private const val KEY = "items"
    fun all(context: Context): List<String> =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getStringSet(KEY, emptySet()).orEmpty().toList()
    fun add(context: Context, value: String) {
        if (value.isBlank()) return
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getStringSet(KEY, emptySet()).orEmpty().toMutableList()
        val updated = (listOf(value) + current.filterNot { it == value }).take(15).toSet()
        prefs.edit().putStringSet(KEY, updated).apply()
    }
    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(KEY).apply()
    }
}
