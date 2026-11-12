package com.typex.ai.languages

/**
 * Offline swipe-path candidate ranker.
 * The UI gesture collector can feed the sequence of nearest QWERTY keys here.
 * This is a lightweight lexical scorer, not a neural swipe model.
 */
class SwipeTypingEngine(private val vocabulary: Set<String> = DEFAULT_WORDS) {
    fun candidates(path: String, limit: Int = 5): List<String> {
        val query = compress(path.filter { it.isLetter() }.lowercase())
        if (query.length < 2) return emptyList()
        return vocabulary.asSequence()
            .map { word -> word to score(query, word) }
            .filter { it.second >= 0.38 }
            .sortedWith(compareByDescending<Pair<String, Double>> { it.second }.thenBy { it.first.length })
            .take(limit)
            .map { it.first }
            .toList()
    }

    private fun compress(value: String): String = buildString {
        value.forEach { if (isEmpty() || last() != it) append(it) }
    }

    private fun score(path: String, word: String): Double {
        if (path == word) return 100.0
        var cursor = 0
        var hits = 0
        for (ch in word) {
            if (cursor < path.length && path[cursor] == ch) {
                hits++
                cursor++
            }
        }
        val coverage = hits.toDouble() / word.length
        val pathCoverage = cursor.toDouble() / path.length
        val lengthPenalty = kotlin.math.abs(path.length - word.length) * 0.08
        return coverage * 0.65 + pathCoverage * 0.35 - lengthPenalty
    }

    companion object {
        val DEFAULT_WORDS = setOf(
            "hello","help","here","hero","home","good","great","green","keyboard",
            "type","typing","there","their","these","those","thanks","thank",
            "beautiful","because","before","between","friend","from","people",
            "please","quick","quiet","question","world","work","welcome","write",
            "android","application","computer","language","love","life","time",
            "today","tomorrow","message","market","mobile","music","phone"
        )
    }
}
