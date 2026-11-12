package com.typex.ai.keyboard

import android.content.Context

object TextShortcutStore {
    private const val PREFS = "typex_text_shortcuts"
    fun all(context: Context): Map<String, String> =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .all.mapNotNull { (k, v) -> (v as? String)?.let { k to it } }.toMap()
    fun put(context: Context, trigger: String, expansion: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(trigger.trim(), expansion).apply()
    }
    fun remove(context: Context, trigger: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(trigger).apply()
    }
}
