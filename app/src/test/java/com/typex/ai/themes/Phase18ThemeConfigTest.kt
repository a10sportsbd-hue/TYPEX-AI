package com.typex.ai.themes

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Phase18ThemeConfigTest {
    @Test fun themeIdsAreUnique() {
        val ids = TypexThemes.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test fun everyThemeHasValidCornerRadius() {
        assertTrue(TypexThemes.all.all { it.cornerRadiusDp in 1..24 })
    }

    @Test fun themePaletteContainsEightPresets() {
        assertEquals(8, TypexThemes.all.size)
    }
}
