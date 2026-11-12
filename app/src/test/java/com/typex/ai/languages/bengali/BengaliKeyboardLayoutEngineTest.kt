package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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
    }
    @Test fun jatiyaEmitsUnicodeDirectly() {
        assertEquals("ক", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "j"))
        assertEquals("ত", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "k"))
        assertEquals("আ", BengaliKeyboardLayoutEngine.outputForKey("jatiya", "h", true))
    }
    @Test fun unknownLayoutFallsBackToAvro() {
        assertEquals("avro", BengaliKeyboardLayoutEngine.normalize("unknown"))
        assertTrue(BengaliKeyboardLayoutEngine.isPhonetic("unknown"))
    }
}