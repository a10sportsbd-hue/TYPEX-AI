package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase28LearningControlsTest {
    @Test fun adaptiveRankingKeepsStableOrderWhenNoLearningScoresExist() {
        assertEquals(listOf("hello", "help", "hero"),
            AdaptivePredictionRanker.rank(listOf("hello", "help", "hero"), emptyMap()))
    }

    @Test fun adaptiveRankingPromotesFrequentlyAcceptedCandidate() {
        assertEquals(listOf("help", "hello", "hero"),
            AdaptivePredictionRanker.rank(listOf("hello", "help", "hero"), mapOf("help" to 9)))
    }

    @Test fun rankingDeduplicatesCandidatesIgnoringCase() {
        assertEquals(listOf("Hello", "world"),
            AdaptivePredictionRanker.rank(listOf("Hello", "hello", "world"), emptyMap()))
    }

    @Test fun rankLimitIsRespected() {
        assertEquals(2, AdaptivePredictionRanker.rank(listOf("a", "b", "c"), emptyMap(), 2).size)
        assertTrue(AdaptivePredictionRanker.rank(listOf("a"), emptyMap(), 0).isEmpty())
    }
}
