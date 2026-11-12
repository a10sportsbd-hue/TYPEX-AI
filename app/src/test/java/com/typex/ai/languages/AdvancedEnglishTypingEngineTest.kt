package com.typex.ai.languages

import org.junit.Assert.*
import org.junit.Test

class AdvancedEnglishTypingEngineTest {
    @Test fun contextPredictsNextWords() {
        val engine = AdvancedEnglishTypingEngine()
        assertEquals(listOf("you"), engine.predictNext(listOf("thank")))
        assertTrue(engine.predictNext(listOf("how", "are")).contains("you"))
    }

    @Test fun prefixPredictionUsesLocalLexicon() {
        val engine = AdvancedEnglishTypingEngine()
        assertTrue(engine.analyze("hel").predictions.contains("hello"))
    }

    @Test fun spellingCorrectionIsSuggestionOnlyAndConservative() {
        val engine = AdvancedEnglishTypingEngine()
        assertEquals("hello", engine.spellingCandidate("helo"))
        assertNull(engine.spellingCandidate("cat"))
        assertNull(engine.spellingCandidate("hello"))
    }

    @Test fun commonContractionsCanBeExpanded() {
        val engine = AdvancedEnglishTypingEngine()
        assertEquals("I'm", engine.expandContraction("im"))
        assertEquals("don't", engine.expandContraction("dont"))
        assertEquals("you're", engine.analyze("youre").expandedContraction)
    }

    @Test fun sentenceCaseOnlyChangesFirstLetterWhenRequested() {
        val engine = AdvancedEnglishTypingEngine()
        assertEquals("Hello there", engine.sentenceCase("hello there"))
        assertEquals("hello there", engine.sentenceCase("hello there", false))
    }

    @Test fun frequencyCanInfluenceSuggestionOrder() {
        val engine = AdvancedEnglishTypingEngine(
            lexicon = setOf("hello", "help", "helmet"),
            frequency = mapOf("helmet" to 10)
        )
        assertEquals("helmet", engine.analyze("hel").predictions.first())
    }
}
