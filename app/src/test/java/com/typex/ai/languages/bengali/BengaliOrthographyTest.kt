package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class BengaliOrthographyTest {
    @Test fun exposesCommonConjuncts() {
        assertEquals("ক্ষ", BengaliOrthography.conjunctFor("ksh"))
        assertEquals("জ্ঞ", BengaliOrthography.conjunctFor("gy"))
        assertEquals("ত্র", BengaliOrthography.conjunctFor("tr"))
        assertNotNull(BengaliOrthography.conjunctFor("ngg"))
    }

    @Test fun unicodeNormalizationIsStable() {
        val word = "বাংলা"
        assertEquals(word, BengaliOrthography.normalizeUnicode(word))
    }

    @Test fun commonDictionaryWordsStayAvailable() {
        assertEquals("আমি", BengaliPhoneticEngine.transliterate("ami"))
        assertEquals("বাংলাদেশ", BengaliPhoneticEngine.transliterate("bangladesh"))
    }
}
