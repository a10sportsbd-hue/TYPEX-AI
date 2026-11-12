package com.typex.ai.languages

import org.junit.Assert.assertTrue
import org.junit.Test

class SwipeTypingEngineTest {
    @Test fun returnsCandidatesForKeyPath() {
        val engine = SwipeTypingEngine(setOf("hello", "help", "world"))
        val result = engine.candidates("helo")
        assertTrue(result.isNotEmpty())
        assertTrue(result.any { it == "hello" || it == "help" })
    }

    @Test fun ignoresVeryShortPath() {
        assertTrue(SwipeTypingEngine().candidates("a").isEmpty())
    }

    @Test fun rejectsUnrelatedPath() {
        val engine = SwipeTypingEngine(setOf("beautiful", "keyboard", "language"))
        assertTrue(engine.candidates("zzzzq").isEmpty())
    }
}
