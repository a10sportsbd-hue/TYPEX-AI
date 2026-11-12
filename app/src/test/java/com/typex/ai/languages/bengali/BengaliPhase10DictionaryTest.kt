package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Test

class BengaliPhase10DictionaryTest {
    @Test fun commonWordsUseExpandedLexicon() {
        assertEquals("তাহলে", BengaliPhoneticEngine.transliterate("tahole"))
        assertEquals("এখানে", BengaliPhoneticEngine.transliterate("ekhane"))
        assertEquals("চট্টগ্রাম", BengaliPhoneticEngine.transliterate("chattogram"))
    }
}
