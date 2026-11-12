package com.typex.ai.keyboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyboardLayoutEngineTest {
    @Test fun providesThreeSupportedPresets() {
        assertEquals(listOf("qwerty", "azerty", "qwertz"), KeyboardLayoutEngine.supportedLayouts)
    }

    @Test fun unknownLayoutFallsBackToQwerty() {
        assertEquals(KeyboardLayoutEngine.rows("qwerty"), KeyboardLayoutEngine.rows("unknown"))
    }

    @Test fun allPresetsContainTheAlphabetExactlyOnce() {
        KeyboardLayoutEngine.supportedLayouts.forEach { layout ->
            val letters = KeyboardLayoutEngine.rows(layout).flatten().filter { it.length == 1 && it[0].isLetter() }
            assertEquals(26, letters.size)
            assertEquals(26, letters.toSet().size)
        }
    }

    @Test fun qwertzSwapsYAndZPositions() {
        val rows = KeyboardLayoutEngine.rows("qwertz")
        assertTrue(rows[0].contains("z"))
        assertTrue(rows[2].contains("y"))
    }
}
