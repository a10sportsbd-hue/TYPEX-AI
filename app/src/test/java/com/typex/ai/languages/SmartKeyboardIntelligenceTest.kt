package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SmartKeyboardIntelligenceTest {
    @Test fun questionWordsPreferQuestionMark() {
        assertEquals(listOf("?", ".", "!"),
            SmartKeyboardIntelligence.punctuationSuggestions("where", false))
        assertEquals(listOf("?", ".", "!"),
            SmartKeyboardIntelligence.punctuationSuggestions("কেন", true))
    }

    @Test fun ordinaryWordsPreferPeriod() {
        assertEquals(listOf(".", "!", "?"),
            SmartKeyboardIntelligence.punctuationSuggestions("hello", false))
    }

    @Test fun englishSentenceStartCapitalizesOnlyFirstLetter() {
        assertEquals("H", SmartKeyboardIntelligence.capitalizeAtSentenceStart("h", true, false))
        assertEquals("h", SmartKeyboardIntelligence.capitalizeAtSentenceStart("h", false, false))
        assertEquals("h", SmartKeyboardIntelligence.capitalizeAtSentenceStart("h", true, true))
    }

    @Test fun punctuationStartsNewSentenceAndSpaceDoesNot() {
        assertTrue(SmartKeyboardIntelligence.nextSentenceStartAfter(".", false))
        assertTrue(SmartKeyboardIntelligence.nextSentenceStartAfter("?", false))
        assertFalse(SmartKeyboardIntelligence.nextSentenceStartAfter(" ", false))
        assertFalse(SmartKeyboardIntelligence.nextSentenceStartAfter("a", true))
    }

    @Test fun completionCapitalizationIsConservative() {
        assertEquals("Hello there",
            SmartKeyboardIntelligence.normalizeCompletion("hello there", true, false))
        assertEquals("hello there",
            SmartKeyboardIntelligence.normalizeCompletion("hello there", false, false))
        assertEquals("আমি ভালো আছি",
            SmartKeyboardIntelligence.normalizeCompletion("আমি ভালো আছি", true, true))
    }
}
