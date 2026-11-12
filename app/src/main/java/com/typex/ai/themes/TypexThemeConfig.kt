package com.typex.ai.themes

import android.content.Context

data class KeyboardTheme(
    val id: String,
    val name: String,
    val background: Long,
    val key: Long,
    val specialKey: Long,
    val text: Long,
    val accent: Long,
    val cornerRadiusDp: Int = 9
)

object TypexThemes {
    val all = listOf(
        KeyboardTheme("midnight", "Midnight Pro", 0xFF0C0C14, 0xFF232231, 0xFF4A3D70, 0xFFF8F6FF, 0xFFBEA9FF),
        KeyboardTheme("neon", "Neon Cyber", 0xFF080D18, 0xFF172B40, 0xFF075E78, 0xFFE7FCFF, 0xFF00E5FF),
        KeyboardTheme("pure", "Pure Minimal", 0xFFECEEF3, 0xFFFFFFFF, 0xFFD9DDF0, 0xFF202333, 0xFF5865C7),
        KeyboardTheme("glass", "Glass Fusion", 0xFF171526, 0xFF343149, 0xFF65548D, 0xFFF7F3FF, 0xFFD6C2FF),
        KeyboardTheme("forest", "Emerald Forest", 0xFF081713, 0xFF183A30, 0xFF28634F, 0xFFE9FFF4, 0xFF71E2AE),
        KeyboardTheme("sunset", "Sunset Glow", 0xFF1B1019, 0xFF422538, 0xFF8A405A, 0xFFFFF0F5, 0xFFFFA6C1),
        KeyboardTheme("ocean", "Ocean Blue", 0xFF081522, 0xFF18334B, 0xFF245B82, 0xFFEAF7FF, 0xFF73C9FF),
        KeyboardTheme("mono", "Monochrome", 0xFF111111, 0xFF303030, 0xFF555555, 0xFFFFFFFF, 0xFFBDBDBD)
    )

    fun get(id: String): KeyboardTheme = all.firstOrNull { it.id == id } ?: all.first()

    fun selected(context: Context): KeyboardTheme {
        val id = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_THEME, "midnight") ?: "midnight"
        return get(id)
    }

    fun save(context: Context, id: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY_THEME, get(id).id).apply()
    }

    fun keyHeight(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_HEIGHT, 46).coerceIn(38, 60)

    fun saveKeyHeight(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putInt(KEY_HEIGHT, value.coerceIn(38, 60)).apply()
    }

    fun keySoundEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_SOUND, false)

    fun setKeySound(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_SOUND, enabled).apply()
    }

    fun vibrationEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_VIBRATION, true)

    fun setVibration(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_VIBRATION, enabled).apply()
    }


    fun keySpacing(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_SPACING, 2).coerceIn(0, 6)

    fun saveKeySpacing(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putInt(KEY_SPACING, value.coerceIn(0, 6)).apply()
    }

    fun toolbarEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_TOOLBAR, true)

    fun setToolbarEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_TOOLBAR, enabled).apply()
    }

    fun compactMode(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_COMPACT, false)

    fun setCompactMode(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_COMPACT, enabled).apply()
    }

    fun oneHandWidth(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_ONE_HAND_WIDTH, 300).coerceIn(250, 350)

    fun saveOneHandWidth(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putInt(KEY_ONE_HAND_WIDTH, value.coerceIn(250, 350)).apply()
    }


    fun swipePreviewEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_SWIPE_PREVIEW, true)

    fun setSwipePreviewEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_SWIPE_PREVIEW, enabled).apply()
    }

    fun keyAnimationsEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_KEY_ANIMATIONS, true)

    fun setKeyAnimationsEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_KEY_ANIMATIONS, enabled).apply()
    }


    fun keyboardLayout(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_LAYOUT, "qwerty") ?: "qwerty"

    fun saveKeyboardLayout(context: Context, layout: String) {
        val safe = layout.lowercase().takeIf { it in setOf("qwerty", "azerty", "qwertz") } ?: "qwerty"
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_LAYOUT, safe).apply()
    }

    fun keyCornerRadius(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_KEY_RADIUS, 9).coerceIn(4, 18)

    fun saveKeyCornerRadius(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY_KEY_RADIUS, value.coerceIn(4, 18)).apply()
    }

    fun keyTextSize(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_TEXT_SIZE, 20).coerceIn(16, 24)

    fun saveKeyTextSize(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY_TEXT_SIZE, value.coerceIn(16, 24)).apply()
    }

    fun keyboardSideInset(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_SIDE_INSET, 5).coerceIn(0, 14)

    fun saveKeyboardSideInset(context: Context, value: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY_SIDE_INSET, value.coerceIn(0, 14)).apply()
    }

    private const val PREFS = "typex_theme_preferences"
    private const val KEY_THEME = "selected_theme"
    private const val KEY_HEIGHT = "key_height_dp"
    private const val KEY_SOUND = "key_sound_enabled"
    private const val KEY_VIBRATION = "key_vibration_enabled"
    private const val KEY_SPACING = "key_spacing_dp"
    private const val KEY_TOOLBAR = "toolbar_enabled"
    private const val KEY_COMPACT = "compact_mode"
    private const val KEY_ONE_HAND_WIDTH = "one_hand_width_dp"
    private const val KEY_SWIPE_PREVIEW = "swipe_preview_enabled"
    private const val KEY_KEY_ANIMATIONS = "key_animations_enabled"
    private const val KEY_LAYOUT = "keyboard_layout"
    private const val KEY_KEY_RADIUS = "key_corner_radius"
    private const val KEY_TEXT_SIZE = "key_text_size"
    private const val KEY_SIDE_INSET = "keyboard_side_inset"
}
