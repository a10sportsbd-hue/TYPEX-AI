package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Test

class AdaptivePredictionRankerTest {
    @Test fun acceptedFrequencyMovesCandidateUp() {
        val ranked = AdaptivePredictionRanker.rank(
            listOf("hello", "help", "helmet"),
            mapOf("helmet" to 12),
            3
        )
        assertEquals(listOf("helmet", "hello", "help"), ranked)
    }

    @Test fun stableOriginalOrderBreaksEqualScoreTies() {
        assertEquals(
            listOf("good", "great", "green"),
            AdaptivePredictionRanker.rank(listOf("good", "great", "green"), emptyMap())
        )
    }

    @Test fun duplicateCandidatesAreRemovedCaseInsensitively() {
        assertEquals(
            listOf("Hello", "help"),
            AdaptivePredictionRanker.rank(listOf("Hello", "hello", "help"), emptyMap())
        )
    }

    @Test fun limitAndEmptyInputAreRespected() {
        assertEquals(emptyList<String>(), AdaptivePredictionRanker.rank(listOf("a"), emptyMap(), 0))
        assertEquals(listOf("a"), AdaptivePredictionRanker.rank(listOf("a", "b"), emptyMap(), 1))
    }
}
