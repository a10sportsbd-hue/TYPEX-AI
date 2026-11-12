package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BengaliSwipeTypingEngineTest {
    private val engine = BengaliSwipeTypingEngine(
        mapOf("ami" to "আমি", "bhalo" to "ভালো", "tumi" to "তুমি")
    )

    @Test fun exactRomanPathReturnsBengaliWord() {
        assertEquals("আমি", engine.candidates("ami", 1).first().second)
    }

    @Test fun tooShortPathIsIgnored() {
        assertTrue(engine.candidates("am").isEmpty())
    }

    @Test fun repeatedAdjacentPathKeysAreNormalized() {
        assertEquals("আমি", engine.candidates("aammii", 1).first().second)
    }
}
