package com.typex.ai.languages.bengali

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BengaliPhase22PipelineTest {
    @Test fun phraseConversionPreservesPunctuationAndSpacing() {
        assertEquals("আমি ভালো আছি!", BengaliTypingPipeline.transliteratePhrase("ami bhalo achi!"))
        assertEquals("তুমি, কোথায়?", BengaliTypingPipeline.transliteratePhrase("tumi, kothay?"))
    }

    @Test fun expandedVocabularyConvertsOffline() {
        assertEquals("প্রযুক্তি", BengaliTypingPipeline.transliteratePhrase("projukti"))
        assertEquals("পৃথিবী", BengaliTypingPipeline.transliteratePhrase("prithibi"))
        assertEquals("শিক্ষা", BengaliTypingPipeline.transliteratePhrase("shikkha"))
    }

    @Test fun romanAliasesNormalizeBeforeConversion() {
        assertEquals("আমি", BengaliTypingPipeline.transliteratePhrase("aami"))
        assertEquals("ধন্যবাদ", BengaliTypingPipeline.transliteratePhrase("onnobad"))
    }

    @Test fun analysisReturnsPrimaryAndAlternatives() {
        val result = BengaliTypingPipeline.analyze("ami")
        assertEquals("আমি", result.primaryCandidate)
        assertTrue(result.alternatives.contains("আমি"))
        assertEquals("আমি", result.convertedText)
    }

    @Test fun unicodeDigitsAndNonRomanSymbolsArePreservedByPhraseMode() {
        assertEquals("আমি 123 🙂", BengaliTypingPipeline.transliteratePhrase("ami 123 🙂"))
    }
}
