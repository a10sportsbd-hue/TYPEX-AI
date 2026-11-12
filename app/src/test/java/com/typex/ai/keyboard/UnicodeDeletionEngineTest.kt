package com.typex.ai.keyboard

import org.junit.Assert.assertEquals
import org.junit.Test

class UnicodeDeletionEngineTest {
    @Test fun deletesOneAsciiCodePoint() {
        assertEquals(1, UnicodeDeletionEngine.codePointsToDelete("hello"))
    }
    @Test fun deletesBengaliBaseAndVowelMarkTogether() {
        assertEquals(2, UnicodeDeletionEngine.codePointsToDelete("কী"))
    }
    @Test fun deletesBengaliConjunctAsOneCluster() {
        assertEquals(3, UnicodeDeletionEngine.codePointsToDelete("ক্ষ"))
    }
    @Test fun deletesConjunctWithTrailingVowelMark() {
        assertEquals(4, UnicodeDeletionEngine.codePointsToDelete("ক্ষি"))
    }
    @Test fun emptyInputDeletesNothing() {
        assertEquals(0, UnicodeDeletionEngine.codePointsToDelete(""))
    }
}
