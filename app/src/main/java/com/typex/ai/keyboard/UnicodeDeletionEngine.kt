package com.typex.ai.keyboard

/**
 * Computes a conservative deletion length for the previous Unicode grapheme-like
 * cluster, with explicit support for combining marks and virama-linked scripts.
 */
object UnicodeDeletionEngine {
    fun codePointsToDelete(textBeforeCursor: CharSequence): Int {
        if (textBeforeCursor.isEmpty()) return 0
        var index = textBeforeCursor.length
        var count = 0

        fun previousCodePoint(): Int {
            val cp = Character.codePointBefore(textBeforeCursor, index)
            index -= Character.charCount(cp)
            count++
            return cp
        }
        fun isMark(cp: Int): Boolean = when (Character.getType(cp)) {
            Character.NON_SPACING_MARK.toInt(),
            Character.COMBINING_SPACING_MARK.toInt(),
            Character.ENCLOSING_MARK.toInt() -> true
            else -> false
        }
        fun consumeMarks() {
            while (index > 0) {
                val cp = Character.codePointBefore(textBeforeCursor, index)
                if (!isMark(cp)) break
                previousCodePoint()
            }
        }

        var last = previousCodePoint()
        if (isMark(last)) {
            consumeMarks()
            if (index > 0) last = previousCodePoint()
        }

        // Bengali virama U+09CD and other Indic viramas link consonants.
        while (index > 0) {
            val previous = Character.codePointBefore(textBeforeCursor, index)
            if (previous == 0x200D || previous == 0x200C) {
                previousCodePoint()
                if (index > 0 && Character.codePointBefore(textBeforeCursor, index) == 0x09CD) {
                    previousCodePoint()
                }
                if (index > 0) previousCodePoint()
                consumeMarks()
                continue
            }
            if (isVirama(previous)) {
                previousCodePoint()
                if (index > 0) previousCodePoint()
                consumeMarks()
                continue
            }
            break
        }
        return count
    }

    private fun isVirama(cp: Int): Boolean = cp in setOf(
        0x09CD, 0x0A4D, 0x0ACD, 0x0B4D, 0x0BCD, 0x0C4D,
        0x0CCD, 0x0D4D, 0x0DCA, 0x1039, 0x17D2
    )
}
