package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertEquals

class BengaliKeyboardLayoutEngineTest {
    @Test fun exposesAllThreeRequestedLayouts() {
        assertEquals(listOf("avro", "probhat", "jatiya"), BengaliKeyboardLayoutEngine.supported)
    }
    @Test fun avroUsesRomanKeysForPhoneticComposition() {
        assertTrue(BengaliKeyboardLayoutEngine.isPhonetic("avro"))
        assertEquals(null, BengaliKeyboardLayoutEngine.outputForKey("avro", "a"))
    }
    @Test fun probhatEmitsUnicodeDirectly() {
        assertFalse(BengaliKeyboardLayoutEngine.isPhonetic("probhat"))
        assertEquals("ক", BengaliKeyboardLayoutEngine.outputForKey("probhat", "k"))
        assertEquals("দ", BengaliKeyboardLayoutEngine.outputForKey("probhat", "q"))
        assertEquals("খ", BengaliKeyboardLayoutEngine.outputForKey("probhat", "k", true))
        assertEquals("অ", BengaliKeyboardLayoutEngine.outputForKey("probhat", "a", true))
        assertEquals("ঙ", BengaliKeyboardLayoutEngine.outputForKey("probhat", "m", true))
    }
    @Test fun jatiyaEmitsUnicodeDirectly() {
        assertEquals("ক", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "j"))
        assertEquals("ত", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "k"))
        assertEquals("অ", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "h", true))
        assertEquals("ৃ", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "a"))
        assertEquals("্", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "g"))
        assertEquals("ফ", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "r", true))
    }
    @Test fun touchDispatchPreservesAvroShiftAndUsesLogicalKeysForDirectLayouts() {
        assertEquals("T", BengaliKeyboardLayoutEngine.touchInput("avro", "t", "T"))
        assertEquals("t", BengaliKeyboardLayoutEngine.touchInput("avro", "t", "t"))
        assertEquals("t", BengaliKeyboardLayoutEngine.touchInput("probhat", "t", "ট"))
        assertEquals("h", BengaliKeyboardLayoutEngine.touchInput("jatiya", "h", "া"))
    }
    @Test fun unknownLayoutFallsBackToAvro() {
        assertEquals("avro", BengaliKeyboardLayoutEngine.normalize("unknown"))
        assertTrue(BengaliKeyboardLayoutEngine.isPhonetic("unknown"))
    }
    @Test fun avroPhoneticPreservesCaseSensitiveRetroflexKeys() {
        assertEquals("ট", BengaliPhoneticEngine.transliterate("T"))
        assertEquals("ঠ", BengaliPhoneticEngine.transliterate("Th"))
        assertEquals("ড", BengaliPhoneticEngine.transliterate("D"))
        assertEquals("ঢ", BengaliPhoneticEngine.transliterate("Dh"))
        assertEquals("ষ", BengaliPhoneticEngine.transliterate("Sh"))
        assertEquals("ত", BengaliPhoneticEngine.transliterate("t"))
    }

}