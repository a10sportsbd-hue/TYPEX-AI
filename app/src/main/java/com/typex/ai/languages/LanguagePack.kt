package com.typex.ai.languages

data class LanguagePack(
    val tag: String,
    val displayName: String,
    val nativeName: String,
    val isAvailable: Boolean,
    val layoutId: String
)

object LanguagePacks {
    val all = listOf(
        LanguagePack("en-US", "English (US)", "English", true, "qwerty"),
        LanguagePack("bn-BD", "Bengali (Bangladesh)", "বাংলা", true, "bengali-phonetic"),
        LanguagePack("ar", "Arabic", "العربية", false, "arabic"),
        LanguagePack("hi-IN", "Hindi", "हिन्दी", false, "hindi"),
        LanguagePack("ur", "Urdu", "اردو", false, "urdu")
    )
}
