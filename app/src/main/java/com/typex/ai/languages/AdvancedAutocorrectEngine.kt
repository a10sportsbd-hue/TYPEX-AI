package com.typex.ai.languages

/**
 * Conservative local autocorrect candidate generator.
 * It returns alternatives only; the keyboard must wait for user selection.
 */
class AdvancedAutocorrectEngine(
    private val englishWords: Set<String> = WordSuggestionEngine.DEFAULT_WORDS,
    private val bengaliRomanWords: Map<String, String> = emptyMap()
) {
    fun englishCandidate(token: String): String? {
        val clean = token.trim().lowercase()
        if (clean.length < 4 || clean in englishWords) return null
        val candidate = englishWords.asSequence()
            .map { it to distance(clean, it) }
            .filter { (word, d) -> d <= (if (clean.length >= 7) 2 else 1) && word.firstOrNull() == clean.firstOrNull() }
            .minWithOrNull(compareBy<Pair<String, Int>> { it.second }.thenBy { it.first.length })
            ?.first
        return candidate?.takeIf { it != clean }
    }

    fun bengaliRomanCandidate(token: String): String? {
        val clean = token.trim().lowercase()
        if (clean.length < 4) return null
        return bengaliRomanWords.entries.asSequence()
            .map { it.key to distance(clean, it.key) }
            .filter { it.second == 1 && it.first.firstOrNull() == clean.firstOrNull() }
            .minByOrNull { it.second }?.let { bengaliRomanWords[it.first] }
    }

    private fun distance(a: String, b: String): Int {
        if (kotlin.math.abs(a.length - b.length) > 2) return 3
        var previous = IntArray(b.length + 1) { it }
        for (i in a.indices) {
            val current = IntArray(b.length + 1)
            current[0] = i + 1
            for (j in b.indices) current[j + 1] = minOf(
                current[j] + 1, previous[j + 1] + 1,
                previous[j] + if (a[i] == b[j]) 0 else 1
            )
            previous = current
        }
        return previous[b.length]
    }
}
