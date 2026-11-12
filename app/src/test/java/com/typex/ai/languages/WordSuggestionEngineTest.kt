package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WordSuggestionEngineTest {
    private val engine = WordSuggestionEngine()

    @Test fun predictsByPrefix() {
        assert(engine.predict("hel").contains("hello"))
    }

    @Test fun suggestsSimpleCorrection() {
        assertEquals("hello", engine.correct("helo"))
    }

    @Test fun knownWordsAreNotCorrected() {
        assertEquals(null, engine.correct("hello"))
    }
}
