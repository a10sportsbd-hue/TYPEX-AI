package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonalVocabularyPredictionTest {
    @Test fun acceptedVocabularyCanBePredictedByPrefix() {
        val engine = WordSuggestionEngine(WordSuggestionEngine.DEFAULT_WORDS + setOf("typexkeyboard"))
        assertTrue(engine.predict("typex").contains("typexkeyboard"))
    }

    @Test fun typoCorrectionIsConservativeForKnownWord() {
        val engine = WordSuggestionEngine()
        assertEquals(null, engine.correct("hello"))
    }

    @Test fun correctionReturnsCandidateForSimpleTypo() {
        val engine = WordSuggestionEngine(setOf("hello", "world"))
        assertEquals("hello", engine.correct("helo"))
    }
}
