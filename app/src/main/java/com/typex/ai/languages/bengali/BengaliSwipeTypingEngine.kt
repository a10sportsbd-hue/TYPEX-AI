package com.typex.ai.languages.bengali

/**
 * Lexicon-backed Bengali swipe candidate generator.
 * Returns Roman dictionary key + Bengali output; it is not a neural swipe model.
 */
class BengaliSwipeTypingEngine(private val dictionary: Map<String, String>) {
    fun candidates(path: String, limit: Int = 5): List<Pair<String, String>> {
        val query = compress(path.filter { it.isLetter() }.lowercase())
        if (query.length < 3) return emptyList()
        return dictionary.entries.asSequence()
            .map { entry -> entry.key to score(query, entry.key) }
            .filter { it.second >= 0.40 }
            .sortedWith(compareByDescending<Pair<String, Double>> { it.second }
                .thenBy { kotlin.math.abs(it.first.length - query.length) })
            .take(limit)
            .map { it.first to dictionary.getValue(it.first) }
            .toList()
    }

    private fun compress(value: String): String = buildString {
        value.forEach { if (isEmpty() || last() != it) append(it) }
    }

    private fun score(path: String, word: String): Double {
        if (path == word) return 1.0
        var cursor = 0
        var matched = 0
        for (char in word) {
            if (cursor < path.length && path[cursor] == char) {
                matched++
                cursor++
            }
        }
        val wordCoverage = matched.toDouble() / word.length
        val pathCoverage = cursor.toDouble() / path.length
        val lengthPenalty = kotlin.math.abs(path.length - word.length) * 0.055
        val firstLetterBonus = if (path.firstOrNull() == word.firstOrNull()) 0.12 else 0.0
        return (wordCoverage * 0.62 + pathCoverage * 0.38 + firstLetterBonus - lengthPenalty)
            .coerceIn(0.0, 1.0)
    }
}
