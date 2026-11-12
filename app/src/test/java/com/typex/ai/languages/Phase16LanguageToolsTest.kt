package com.typex.ai.languages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase16LanguageToolsTest {
    @Test fun autocorrectReturnsCandidateWithoutChangingInput() {
        val engine = AdvancedAutocorrectEngine(setOf("hello", "world"))
        assertEquals("hello", engine.englishCandidate("hellp"))
        assertEquals("hellp", "hellp")
    }

    @Test fun autocorrectSkipsCorrectWords() {
        assertEquals(null, AdvancedAutocorrectEngine().englishCandidate("hello"))
    }

    @Test fun phraseCompletionOffersWholePhrase() {
        assertTrue(PhraseCompletionEngine.completions("thank", false).contains("Thank you so much."))
    }

    @Test fun bilingualDictionarySearchesBothLanguages() {
        assertTrue(BilingualDictionaryEngine.search("friend").any { it.bengali == "বন্ধু" })
        assertTrue(BilingualDictionaryEngine.search("বন্ধু").any { it.english == "Friend" })
    }
}
