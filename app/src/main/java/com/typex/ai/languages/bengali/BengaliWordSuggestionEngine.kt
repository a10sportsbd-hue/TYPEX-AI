package com.typex.ai.languages.bengali

/**
 * Offline Bengali candidate ranker using a local lexicon, conservative typo
 * matching and small transparent context hints. Not a neural language model.
 */
class BengaliWordSuggestionEngine(
    private val dictionary: Map<String, String> = BengaliPhoneticEngine.dictionaryEntries(),
    private val contextHints: Map<String, List<String>> = DEFAULT_CONTEXT
) {
    fun suggest(romanPrefix: String, previousBengaliWord: String? = null, limit: Int = 5): List<String> {
        if (limit <= 0) return emptyList()
        val query = BengaliTypingPipeline.normalizeRoman(romanPrefix).trim().lowercase()
        if (query.isEmpty()) return emptyList()

        val ranked = dictionary.entries
            .mapNotNull { entry ->
                val distance = editDistance(query, entry.key)
                val prefix = entry.key.startsWith(query)
                if (!prefix && distance > typoLimit(query.length)) null
                else Triple(entry.value, when {
                    entry.key == query -> 0
                    prefix -> 10 + (entry.key.length - query.length)
                    else -> 30 + distance
                }, entry.key.length)
            }
            .sortedWith(compareBy<Triple<String, Int, Int>> { it.second }.thenBy { it.third })
            .map { it.first }

        val exact = dictionary[query]
        val direct = BengaliPhoneticEngine.transliterate(query)
        val contextual = previousBengaliWord?.let { contextHints[it].orEmpty() }.orEmpty()
        return buildList {
            if (exact != null) add(exact)
            if (direct.isNotBlank()) add(direct)
            addAll(ranked)
            addAll(contextual)
            addAll(BengaliPhoneticEngine.suggestions(query, limit))
        }.distinct().take(limit)
    }

    /**
     * Candidate lookup for direct-Unicode layouts (Probhat/Jatiya).
     * Matches the Bengali text already present in the editor; it does not
     * infer or fabricate a Roman composition buffer.
     */
    fun suggestNativePrefix(bengaliPrefix: String, limit: Int = 5): List<String> {
        if (limit <= 0) return emptyList()
        val query = BengaliOrthography.normalizeUnicode(bengaliPrefix.trim())
        if (query.isEmpty()) return emptyList()
        return dictionary.values.asSequence()
            .map(BengaliOrthography::normalizeUnicode)
            .distinct()
            .filter { it.startsWith(query) && it != query }
            .sortedWith(compareBy<String> { it.length }.thenBy { it })
            .take(limit)
            .toList()
    }

    private fun typoLimit(length: Int) = if (length >= 5) 1 else 0

    private fun editDistance(a: String, b: String): Int {
        var previous = IntArray(b.length + 1) { it }
        for (i in a.indices) {
            val current = IntArray(b.length + 1)
            current[0] = i + 1
            for (j in b.indices) {
                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + if (a[i] == b[j]) 0 else 1
                )
            }
            previous = current
        }
        return previous[b.length]
    }

    companion object {
        val DEFAULT_CONTEXT = mapOf(
            "আমি" to listOf("ভালো", "আছি", "বাংলা", "যাব", "করব", "চাই"),
            "তুমি" to listOf("কেমন", "কোথায়", "কি", "আছো", "কীভাবে"),
            "আপনি" to listOf("কেমন", "আছেন", "কোথায়", "কি"),
            "আমরা" to listOf("সবাই", "একসাথে", "যাব", "করব"),
            "বাংলাদেশ" to listOf("আমার", "সুন্দর", "দেশ", "অনেক"),
            "ধন্যবাদ" to listOf("আপনাকে", "অনেক", "জানাই"),
            "ভালো" to listOf("আছি", "লাগছে", "লাগলো"),
            "আজকে" to listOf("আমি", "অনেক", "কাজ", "যাব"),
            "আসসালামু" to listOf("আলাইকুম")
        )
    }
}
