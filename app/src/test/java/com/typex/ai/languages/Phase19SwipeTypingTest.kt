package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase19SwipeTypingTest {
    @Test fun repeatedAdjacentPathKeysAreCompressed() {
        assertEquals("hello", SwipeTypingEngine(setOf("hello")).candidates("heelllo", 1).first())
    }

    @Test fun unrelatedPathDoesNotProduceCandidate() {
        assertTrue(SwipeTypingEngine(setOf("keyboard")).candidates("zzzzq", 3).isEmpty())
    }
}
