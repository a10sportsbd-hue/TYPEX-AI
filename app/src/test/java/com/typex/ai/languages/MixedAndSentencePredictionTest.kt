package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MixedLanguageEngineTest {
    @Test fun knownBengaliPhoneticWordConverts() {
        assertEquals("আমি", MixedLanguageEngine.resolveBengaliModeToken("ami"))
    }

    @Test fun commonEnglishWordStaysEnglishInBengaliMode() {
        assertEquals("hello", MixedLanguageEngine.resolveBengaliModeToken("hello"))
    }

    @Test fun approvedEnglishWordStaysEnglish() {
        assertEquals(
            "mybrand",
            MixedLanguageEngine.resolveBengaliModeToken("mybrand", knownEnglishWords = setOf("mybrand"))
        )
    }

    @Test fun customBengaliMappingHasPriority() {
        assertEquals(
            "আমার শব্দ",
            MixedLanguageEngine.resolveBengaliModeToken("myword", customBengali = mapOf("myword" to "আমার শব্দ"))
        )
    }
}

class SentencePredictionEngineTest {
    @Test fun englishNextWordHints() {
        assertTrue(SentencePredictionEngine.nextWords("thank", false).contains("you"))
    }

    @Test fun bengaliNextWordHints() {
        assertTrue(SentencePredictionEngine.nextWords("আমি", true).contains("ভালো"))
    }

    @Test fun unknownContextReturnsNoGuess() {
        assertTrue(SentencePredictionEngine.nextWords("unknown", false).isEmpty())
    }
}
