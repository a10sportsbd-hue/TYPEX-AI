package com.typex.ai.languages.bengali

/**
 * Direct Bengali key maps for Avro-phonetic, Probhat and National/Jatiya input modes.
 * Avro uses the Latin QWERTY key positions and feeds the phonetic composition engine.
 * Probhat and Jatiya emit Unicode Bengali characters directly.
 */
object BengaliKeyboardLayoutEngine {
    const val AVRO = "avro"
    const val PROBHAT = "probhat"
    const val JATIYA = "jatiya"
    val supported = listOf(AVRO, PROBHAT, JATIYA)

    private val qwertyRows = listOf(
        listOf("q","w","e","r","t","y","u","i","o","p"),
        listOf("a","s","d","f","g","h","j","k","l"),
        listOf("⇧","z","x","c","v","b","n","m","⌫")
    )

    private val probhat = mapOf(
        "q" to "দ", "w" to "ূ", "e" to "ী", "r" to "র", "t" to "ট", "y" to "এ", "u" to "ু", "i" to "ি", "o" to "ও", "p" to "প",
        "a" to "া", "s" to "স", "d" to "ড", "f" to "ত", "g" to "গ", "h" to "হ", "j" to "জ", "k" to "ক", "l" to "ল",
        "z" to "য়", "x" to "শ", "c" to "চ", "v" to "আ", "b" to "ব", "n" to "ন", "m" to "ম"
    )
    private val probhatShift = mapOf(
        "q" to "ধ", "w" to "ঊ", "e" to "ঈ", "r" to "ড়", "t" to "ঠ", "y" to "ঐ", "u" to "উ", "i" to "ই", "o" to "ঔ", "p" to "ফ",
        "a" to "ঌ", "s" to "ৡ", "d" to "ৄ", "f" to "ত", "g" to "ঘ", "h" to "ঽ", "j" to "ঝ", "k" to "খ", "l" to "ং",
        "z" to "য়", "x" to "ষ", "c" to "ছ", "v" to "অ", "b" to "ভ", "n" to "ণ", "m" to "ঙ"
    )
    private val jatiya = mapOf(
        "q" to "ং", "w" to "য়", "e" to "ঢ", "r" to "প", "t" to "ঠ", "y" to "ছ", "u" to "ঝ", "i" to "ঞ", "o" to "ঘ", "p" to "ঢ়",
        "a" to "ৃ", "s" to "ু", "d" to "ি", "f" to "ব", "g" to "্", "h" to "া", "j" to "ক", "k" to "ত", "l" to "দ",
        "z" to "ঁ", "x" to "ো", "c" to "ে", "v" to "র", "b" to "ন", "n" to "স", "m" to "ম"
    )
    private val jatiyaShift = mapOf(
        "q" to "ৣ", "w" to "ঊ", "e" to "ঈ", "r" to "ফ", "t" to "ঠ", "y" to "ঐ", "u" to "ঝ", "i" to "ঞ", "o" to "ঘ", "p" to "ঢ়",
        "a" to "ঋ", "s" to "উ", "d" to "ই", "f" to "ৰ", "g" to "॥", "h" to "আ", "j" to "ক", "k" to "ত", "l" to "ঌ",
        "z" to "৺", "x" to "ঔ", "c" to "এ", "v" to "ল", "b" to "ণ", "n" to "ষ", "m" to "শ"
    )

    fun normalize(layout: String): String = layout.lowercase().takeIf { it in supported } ?: AVRO
    fun rows(layout: String): List<List<String>> = qwertyRows
    fun isPhonetic(layout: String): Boolean = normalize(layout) == AVRO
    fun outputForKey(layout: String, key: String, shifted: Boolean = false): String? {
        val id = normalize(layout)
        if (id == AVRO) return null
        val k = key.lowercase()
        return when (id) {
            PROBHAT -> if (shifted) probhatShift[k] else probhat[k]
            JATIYA -> if (shifted) jatiyaShift[k] else jatiya[k]
            else -> null
        }
    }
    fun displayForKey(layout: String, key: String, shifted: Boolean = false): String =
        outputForKey(layout, key, shifted) ?: key
    fun layoutName(layout: String): String = when (normalize(layout)) {
        PROBHAT -> "প্রভাত"
        JATIYA -> "জাতীয়"
        else -> "Avro"
    }
}