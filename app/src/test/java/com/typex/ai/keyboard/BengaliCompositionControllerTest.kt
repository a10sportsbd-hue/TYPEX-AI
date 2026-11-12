package com.typex.ai.keyboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BengaliCompositionControllerTest {
    @Test fun appendBuildsLiveBengaliPreview() {
        val input = BengaliCompositionController()
        assertEquals("আ", input.append("a"))
        assertEquals("আম", input.append("m"))
        assertEquals("আমি", input.append("i"))
    }

    @Test fun commitReturnsTextAndResetsComposition() {
        val input = BengaliCompositionController()
        input.append("a"); input.append("m"); input.append("i")
        assertEquals("আমি", input.commit())
        assertTrue(input.isEmpty())
    }

    @Test fun backspaceRemovesOnlyLastRomanCharacter() {
        val input = BengaliCompositionController()
        input.append("a"); input.append("m"); input.append("i")
        assertTrue(input.backspace())
        assertEquals("am", input.raw())
        assertEquals("আম", input.preview())
    }

    @Test fun backspaceOnEmptyCompositionIsSafe() {
        assertFalse(BengaliCompositionController().backspace())
    }

    @Test fun customBengaliWordOverridesFallbackTransliteration() {
        val input = BengaliCompositionController()
        "xyz".forEach { input.append(it.toString(), mapOf("xyz" to "পরীক্ষা")) }
        assertEquals("পরীক্ষা", input.preview(mapOf("xyz" to "পরীক্ষা")))
    }
}