package com.typex.ai.languages.bengali

/**
 * Conservative Bengali orthographic helpers.
 * These rules are opt-in utilities; transliteration output is not blindly
 * rewritten because many sequences are context dependent.
 */
object BengaliOrthography {
    private val romanClusters = linkedMapOf(
        "ksh" to "ক্ষ", "kkh" to "ক্ষ", "gy" to "জ্ঞ", "jn" to "জ্ঞ",
        "ngk" to "ঙ্ক", "ngg" to "ঙ্গ", "nj" to "ঞ্জ", "nd" to "ন্দ",
        "nt" to "ন্ত", "mp" to "ম্প", "mb" to "ম্ব",
        "kr" to "ক্র", "gr" to "গ্র", "tr" to "ত্র",
        "dr" to "দ্র", "pr" to "প্র", "br" to "ব্র",
        "shr" to "শ্র", "str" to "স্ত্র"
    )

    fun knownRomanClusters(): Map<String, String> = romanClusters.toMap()

    /** Return the conjunct glyph sequence for a supported Roman cluster. */
    fun conjunctFor(romanCluster: String): String? =
        romanClusters[romanCluster.trim().lowercase()]

    /** A safe NFC normalization pass for composed Bengali text. */
    fun normalizeUnicode(text: String): String =
        java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFC)
}
