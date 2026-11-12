package com.typex.ai.languages.bengali

import org.junit.Assert.assertTrue
import org.junit.Test

class BengaliPhase25SuggestionIntegrationTest {
    @Test fun typedRomanCandidateUsesPreviousBengaliContext() {
        val engine = BengaliWordSuggestionEngine(
            dictionary = mapOf("bhalo" to "ভালো", "bangla" to "বাংলা")
        )
        val results = engine.suggest(
            romanPrefix = "b",
            previousBengaliWord = "আমি",
            limit = 5
        )
        assertTrue(results.contains("ভালো"))
    }

    @Test fun contextualHintsCoverCommonBengaliConversation() {
        val engine = BengaliWordSuggestionEngine(dictionary = emptyMap())
        val results = engine.suggest(
            romanPrefix = "xyz",
            previousBengaliWord = "তুমি",
            limit = 5
        )
        assertTrue(results.contains("কেমন"))
        assertTrue(results.contains("আছো"))
    }

    @Test fun pipelineAndCustomDictionaryCandidatesCanBeCombined() {
        val custom = mapOf("amar" to "আমার")
        val engine = BengaliWordSuggestionEngine(
            dictionary = BengaliPhoneticEngine.dictionaryEntries() + custom
        )
        assertTrue(engine.suggest("amar", limit = 5).contains("আমার"))
    }
}
