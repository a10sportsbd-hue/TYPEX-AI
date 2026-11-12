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
}
