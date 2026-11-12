package com.typex.ai.languages.bengali

import org.junit.Assert.assertTrue
import org.junit.Test

class BengaliWordSuggestionEngineTest {
    @Test fun prefixReturnsBengaliCandidates() {
        val engine = BengaliWordSuggestionEngine(mapOf(
            "tumi" to "তুমি", "tomar" to "তোমার", "tahole" to "তাহলে"
        ))
        val results = engine.suggest("to")
        assertTrue(results.contains("তুমি") || results.contains("তোমার"))
    }

    @Test fun contextHintsAreIncluded() {
        val engine = BengaliWordSuggestionEngine(emptyMap())
        assertTrue(engine.suggest("x", previousBengaliWord = "আমি").contains("ভালো"))
    }
    @Test fun nativePrefixSuggestsOnlyLongerBengaliWords() {
        val engine = BengaliWordSuggestionEngine(mapOf(
            "ami" to "আমি",
            "amar" to "আমার",
            "amake" to "আমাকে",
            "tumi" to "তুমি"
        ))
        val results = engine.suggestNativePrefix("আম")
        assertTrue(results.contains("আমার"))
        assertTrue(results.contains("আমাকে"))
        assertTrue(results.contains("আমি"))
        assertTrue(!results.contains("তুমি"))
    }

    @Test fun nativePrefixHonorsLimitAndEmptyInput() {
        val engine = BengaliWordSuggestionEngine(mapOf(
            "amar" to "আমার", "amake" to "আমাকে", "amader" to "আমাদের"
        ))
        assertTrue(engine.suggestNativePrefix("").isEmpty())
        assertTrue(engine.suggestNativePrefix("আম", limit = 1).size <= 1)
    }

}
