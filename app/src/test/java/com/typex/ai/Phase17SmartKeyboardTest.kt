package com.typex.ai

import com.typex.ai.keyboard.EmojiSearchEngine
import com.typex.ai.languages.bengali.BengaliAutocorrectEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase17SmartKeyboardTest {
    @Test fun bengaliAutocorrectSuggestsNearRomanSpelling() {
        val engine = BengaliAutocorrectEngine(mapOf(
            "bhalo" to "ভালো",
            "ami" to "আমি",
            "tumi" to "তুমি"
        ))
        assertEquals("ভালো", engine.candidate("bhaloo")?.second)
    }

    @Test fun bengaliAutocorrectDoesNotSuggestForKnownWord() {
        val engine = BengaliAutocorrectEngine(mapOf("bhalo" to "ভালো"))
        assertEquals(null, engine.candidate("bhalo"))
    }

    @Test fun emojiSearchSupportsBengaliKeywords() {
        assertTrue(EmojiSearchEngine.search("ভালোবাসা").any { it.emoji == "❤️" })
    }

    @Test fun emojiSearchSupportsEnglishKeywords() {
        assertTrue(EmojiSearchEngine.search("coffee").any { it.emoji == "☕" })
    }
}
