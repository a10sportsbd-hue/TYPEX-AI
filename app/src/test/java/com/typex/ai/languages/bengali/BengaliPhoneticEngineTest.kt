package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Test

class BengaliPhoneticEngineTest {
    @Test fun commonWordsConvertOffline() {
        assertEquals("আমি", BengaliPhoneticEngine.transliterate("ami"))
        assertEquals("তুমি", BengaliPhoneticEngine.transliterate("tumi"))
        assertEquals("বাংলা", BengaliPhoneticEngine.transliterate("bangla"))
        assertEquals("ভালো", BengaliPhoneticEngine.transliterate("bhalo"))
        assertEquals("ধন্যবাদ", BengaliPhoneticEngine.transliterate("dhonnobad"))
    }

    @Test fun suggestionsIncludeConvertedWord() {
        assert(BengaliPhoneticEngine.suggestions("ami").contains("আমি"))
    }
    @Test fun caseSensitiveAvroKeysArePreserved() {
        assertEquals("ট", BengaliPhoneticEngine.transliterate("T"))
        assertEquals("ঠ", BengaliPhoneticEngine.transliterate("Th"))
        assertEquals("ঞ", BengaliPhoneticEngine.transliterate("NG"))
        assertEquals("ঙ", BengaliPhoneticEngine.transliterate("Ng"))
        assertEquals("ঈ", BengaliPhoneticEngine.transliterate("I"))
    }

    @Test fun normalizationDoesNotDestroyAvroCase() {
        assertEquals("Th", BengaliPhoneticEngine.normalizeRomanInput("  Th  "))
        assertEquals("to", BengaliPhoneticEngine.normalizeRomanInput("tw"))
    }

    @Test fun suggestionsKeepCaseSensitiveConversion() {
        assertEquals("ঠ", BengaliPhoneticEngine.suggestions("Th").first())
    }

}
