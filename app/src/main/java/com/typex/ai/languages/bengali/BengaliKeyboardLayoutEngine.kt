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
        "a" to "া", "b" to "ব", "c" to "চ", "d" to "ড", "e" to "ী", "f" to "ত",
        "g" to "গ", "h" to "হ", "i" to "ি", "j" to "জ", "k" to "ক", "l" to "ল",
        "m" to "ম", "n" to "ন", "o" to "ও", "p" to "প", "q" to "দ", "r" to "র",
        "s" to "স", "t" to "ট", "u" to "ু", "v" to "আ", "w" to "ূ", "x" to "শ",
        "y" to "এ", "z" to "য়"
    )
    private val probhatShift = mapOf(
        "a" to "অ", "b" to "ভ", "c" to "ছ", "d" to "ঢ", "e" to "ঈ", "f" to "থ",
        "g" to "ঘ", "h" to "ঃ", "i" to "ই", "j" to "ঝ", "k" to "খ", "l" to "ং",
        "m" to "ঙ", "n" to "ণ", "o" to "ঔ", "p" to "ফ", "q" to "ধ", "r" to "ড়",
        "s" to "ষ", "t" to "ঠ", "u" to "উ", "v" to "ঋ", "w" to "ঊ", "x" to "ঢ়",
        "y" to "ঐ", "z" to "য"
    )
    private val jatiya = mapOf(
        "a" to "ৃ", "b" to "ন", "c" to "ে", "d" to "ি", "e" to "ড", "f" to "ব",
        "g" to "্", "h" to "া", "i" to "হ", "j" to "ক", "k" to "ত", "l" to "দ",
        "m" to "ম", "n" to "স", "o" to "গ", "p" to "ড়", "q" to "ঙ", "r" to "প",
        "s" to "ু", "t" to "ট", "u" to "জ", "v" to "র", "w" to "য", "x" to "ো",
        "y" to "চ", "z" to "ঁ"
    )
    private val jatiyaShift = mapOf(
        "a" to "ৗ", "b" to "ণ", "c" to "ৈ", "d" to "ী", "e" to "ঢ", "f" to "ভ",
        "g" to "।", "h" to "অ", "i" to "ঞ", "j" to "খ", "k" to "থ", "l" to "ধ",
        "m" to "শ", "n" to "ষ", "o" to "ঘ", "p" to "ঢ়", "q" to "ং", "r" to "ফ",
        "s" to "ূ", "t" to "ঠ", "u" to "ঝ", "v" to "ল", "w" to "য়", "x" to "ৌ",
        "y" to "ছ", "z" to "ঃ"
    )

    fun normalize(layout: String): String = layout.lowercase().takeIf { it in supported } ?: AVRO
    fun rows(layout: String): List<List<String>> = qwertyRows
    fun isPhonetic(layout: String): Boolean = normalize(layout) == AVRO
    /** Returns the logical Latin key for direct layouts, or the displayed key for Avro. */
    fun touchInput(layout: String, logicalKey: String, displayedKey: String): String =
        if (isPhonetic(layout)) displayedKey else logicalKey
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