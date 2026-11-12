package com.typex.ai.languages

/**
 * Small offline word prediction and correction engine.
 * Dictionary can be extended from local user vocabulary.
 */
class WordSuggestionEngine(
    private val words: Set<String> = DEFAULT_WORDS,
    private val preferredRank: Map<String, Int> = emptyMap()
) {
    fun predict(prefix: String, limit: Int = 5): List<String> {
        val q = prefix.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return words.asSequence()
            .filter { it.startsWith(q) && it != q }
            .sortedWith(
                compareByDescending<String> { preferredRank[it] ?: 0 }
                    .thenBy { it.length }.thenBy { it }
            )
            .take(limit)
            .toList()
    }

    fun correct(word: String): String? {
        val q = word.trim().lowercase()
        if (q.isEmpty() || q in words) return null
        return words.asSequence()
            .map { it to levenshtein(q, it) }
            .filter { it.second <= threshold(q.length) }
            .minWithOrNull(
                compareBy<Pair<String, Int>> { it.second }
                    .thenByDescending { isSubsequence(q, it.first) }
                    .thenByDescending { preferredRank[it.first] ?: 0 }
                    .thenBy { it.first.length }
            )
            ?.first
    }

    fun addWord(word: String): Set<String> = words + word.trim().lowercase()

    private fun isSubsequence(needle: String, haystack: String): Boolean {
        var index = 0
        for (character in haystack) {
            if (index < needle.length && needle[index] == character) index++
        }
        return index == needle.length
    }

    private fun threshold(length: Int): Int = when {
        length < 4 -> 0
        length <= 7 -> 1
        else -> 2
    }

    private fun levenshtein(a: String, b: String): Int {
        val previous = IntArray(b.length + 1) { it }
        for (i in a.indices) {
            val current = IntArray(b.length + 1)
            current[0] = i + 1
            for (j in b.indices) {
                val cost = if (a[i] == b[j]) 0 else 1
                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + cost
                )
            }
            for (j in previous.indices) previous[j] = current[j]
        }
        return previous[b.length]
    }

    companion object {
        val DEFAULT_WORDS = setOf(
            "about", "after", "again", "all", "also", "and", "are", "because",
            "been", "before", "being", "can", "come", "could", "day", "dear",
            "do", "does", "done", "each", "every", "for", "from", "good",
            "great", "have", "hello", "help", "here", "how", "important",
            "into", "just", "know", "like", "love", "make", "many", "more",
            "most", "much", "need", "nice", "not", "now", "one", "only",
            "please", "really", "right", "thank", "thanks", "that", "the",
            "their", "there", "this", "time", "today", "tomorrow", "very",
            "want", "was", "what", "when", "where", "which", "who", "will",
            "with", "work", "would", "you", "your"
        )
    }
}
