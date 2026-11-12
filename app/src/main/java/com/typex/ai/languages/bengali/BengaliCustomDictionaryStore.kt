package com.typex.ai.languages.bengali

import android.content.Context

object BengaliCustomDictionaryStore {
    private const val PREFS = "typex_bn_custom_dictionary"
    fun all(context: Context): Map<String, String> =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .all.mapNotNull { (key, value) -> (value as? String)?.let { key to it } }.toMap()
    fun put(context: Context, roman: String, bengali: String) {
        val key = roman.trim().lowercase()
        if (key.isNotBlank() && bengali.isNotBlank()) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(key, bengali.trim()).apply()
        }
    }
    fun remove(context: Context, roman: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(roman).apply()
    }
}
