package com.typex.ai.keyboard

/**
 * Built-in keyboard layout presets. These are fixed layouts, not a drag-and-drop editor.
 */
object KeyboardLayoutEngine {
    val supportedLayouts = listOf("qwerty", "azerty", "qwertz")

    fun rows(layout: String): List<List<String>> = when (layout.lowercase()) {
        "azerty" -> listOf(
            listOf("a","z","e","r","t","y","u","i","o","p"),
            listOf("q","s","d","f","g","h","j","k","l","m"),
            listOf("⇧","w","x","c","v","b","n","⌫")
        )
        "qwertz" -> listOf(
            listOf("q","w","e","r","t","z","u","i","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("⇧","y","x","c","v","b","n","m","⌫")
        )
        else -> listOf(
            listOf("q","w","e","r","t","y","u","i","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("⇧","z","x","c","v","b","n","m","⌫")
        )
    }
}
