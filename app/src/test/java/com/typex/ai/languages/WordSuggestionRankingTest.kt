package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Test

class WordSuggestionRankingTest {
    @Test fun acceptedWordRankIsAppliedBeforeLengthTieBreak() {
        val engine = WordSuggestionEngine(
            words = setOf("testing", "testcase"),
            preferredRank = mapOf("testcase" to 8, "testing" to 2)
        )
        assertEquals("testcase", engine.predict("test", 1).first())
    }
}
