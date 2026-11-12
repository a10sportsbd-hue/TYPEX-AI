package com.typex.ai.languages

/**
 * Ranks already-generated candidates using explicit local acceptance counts.
 * Frequency contributes a bounded tie-break boost; it cannot replace the
 * lexical/context relevance ordering when scores are close.
 */
object AdaptivePredictionRanker {
    fun rank(
        candidates: List<String>,
        usageScores: Map<String, Int>,
        limit: Int = candidates.size
    ): List<String> {
        if (limit <= 0) return emptyList()
        return candidates.withIndex()
            .distinctBy { it.value.lowercase() }
            .sortedWith(
                compareByDescending<IndexedValue<String>> {
                    usageScores[it.value.lowercase()]?.coerceIn(0, 10_000) ?: 0
                }.thenBy { it.index }
            )
            .take(limit)
            .map { it.value }
    }
}
