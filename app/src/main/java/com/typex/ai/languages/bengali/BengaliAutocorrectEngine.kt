package com.typex.ai.languages.bengali

/**
 * Conservative Roman-to-Bengali spelling helper.
 * It proposes a lexicon candidate; it never changes committed text by itself.
 */
class BengaliAutocorrectEngine(
    private val words: Map<String, String> = BengaliPhoneticEngine.dictionaryEntries()
) {
    fun candidate(input: String): Pair<String, String>? {
        val q = input.trim().lowercase()
        if (q.length < 4 || q in words) return null
        return words.keys.asSequence()
            .map { it to distance(q, it) }
            .filter { (word, d) -> d == 1 && word.firstOrNull() == q.firstOrNull() }
            .minByOrNull { it.second }
            ?.let { it.first to words.getValue(it.first) }
    }

    private fun distance(a: String, b: String): Int {
        if (kotlin.math.abs(a.length - b.length) > 1) return 2
        var prev = IntArray(b.length + 1) { it }
        for (i in a.indices) {
            val cur = IntArray(b.length + 1)
            cur[0] = i + 1
            for (j in b.indices) cur[j + 1] = minOf(
                cur[j] + 1, prev[j + 1] + 1,
                prev[j] + if (a[i] == b[j]) 0 else 1
            )
            prev = cur
        }
        return prev[b.length]
    }
}
