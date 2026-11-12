package com.typex.ai.languages.bengali

/**
 * Phase 22 Bengali typing pipeline.
 *
 * Combines deterministic Roman normalization, word conversion, phrase handling,
 * candidate ranking and conservative Unicode normalization. This is an offline
 * rule/lexicon engine, not a complete Avro clone or a neural language model.
 */
object BengaliTypingPipeline {

    data class Result(
        val romanInput: String,
        val convertedText: String,
        val primaryCandidate: String,
        val alternatives: List<String>
    )

    private val aliases = mapOf(
        "ami" to "ami", "aami" to "ami",
        "tumi" to "tumi", "tmi" to "tumi",
        "valo" to "valo", "bhalo" to "bhalo",
        "shundor" to "shundor", "sundor" to "sundor",
        "kothai" to "kothay", "kothay" to "kothay",
        "kisu" to "kisu", "kichu" to "kichu",
        "dhonnobad" to "dhonnobad", "onnobad" to "dhonnobad"
    )

    private val extraWords = mapOf(
        "ami" to "আমি", "amio" to "আমিও", "amra" to "আমরা",
        "tumi" to "তুমি", "tomra" to "তোমরা", "apni" to "আপনি",
        "apnara" to "আপনারা", "bhalo" to "ভালো", "valo" to "ভালো",
        "kharap" to "খারাপ", "shundor" to "সুন্দর", "sundor" to "সুন্দর",
        "prithibi" to "পৃথিবী", "bangladesh" to "বাংলাদেশ",
        "bangla" to "বাংলা", "banglay" to "বাংলায়",
        "manush" to "মানুষ", "bondhu" to "বন্ধু", "poribar" to "পরিবার",
        "ma" to "মা", "baba" to "বাবা", "bhai" to "ভাই", "bon" to "বোন",
        "shikkha" to "শিক্ষা", "shikkhito" to "শিক্ষিত",
        "projukti" to "প্রযুক্তি", "biggan" to "বিজ্ঞান",
        "shomoy" to "সময়", "somoy" to "সময়", "aj" to "আজ",
        "ajke" to "আজকে", "kal" to "কাল", "kalke" to "কালকে",
        "ekhon" to "এখন", "ekhane" to "এখানে", "sekhan" to "সেখানে",
        "kothay" to "কোথায়", "keno" to "কেন", "kivabe" to "কিভাবে",
        "ki" to "কি", "na" to "না", "hoy" to "হয়", "hobe" to "হবে",
        "korbo" to "করব", "korchi" to "করছি", "korechi" to "করেছি",
        "jacchi" to "যাচ্ছি", "jabo" to "যাব", "asche" to "আসছে",
        "likhbo" to "লিখব", "shikhbo" to "শিখব", "bujhlam" to "বুঝলাম",
        "dhonnobad" to "ধন্যবাদ", "assalamu" to "আসসালামু",
        "alaikum" to "আলাইকুম", "inshallah" to "ইনশাআল্লাহ"
    )

    init {
        extraWords.forEach { (roman, bengali) ->
            BengaliPhoneticEngine.addDictionaryEntry(roman, bengali)
        }
    }

    fun normalizeRoman(input: String): String {
        val normalized = input.trim().lowercase()
            .replace('অ', 'o')
            .replace(Regex("\\s+"), " ")
        return normalized.split(" ").joinToString(" ") { token ->
            aliases[token] ?: token
        }
    }

    /** Converts a complete phrase while preserving whitespace and punctuation. */
    fun transliteratePhrase(input: String): String {
        if (input.isEmpty()) return ""
        val parts = Regex("([\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+)")
            .findAll(input)
        return buildString {
            parts.forEach { match ->
                val token = match.value
                val converted = if (token.any { it.isLetter() } &&
                    token.all { it.isLetter() } &&
                    token.all { it.code < 128 }
                ) {
                    val key = normalizeRoman(token)
                    BengaliPhoneticEngine.transliterate(key)
                } else token
                append(converted)
            }
        }.let(BengaliOrthography::normalizeUnicode)
    }

    fun analyze(romanInput: String, previousBengaliWord: String? = null, limit: Int = 5): Result {
        val normalized = normalizeRoman(romanInput)
        val converted = transliteratePhrase(normalized)
        val words = normalized.split(Regex("\\s+")).filter(String::isNotBlank)
        val lastWord = words.lastOrNull().orEmpty()
        val engine = BengaliWordSuggestionEngine()
        val candidates = if (lastWord.isEmpty()) emptyList() else {
            val ranked = engine.suggest(lastWord, previousBengaliWord, limit)
            val direct = BengaliPhoneticEngine.transliterate(lastWord)
            (listOf(direct) + ranked).filter(String::isNotBlank).distinct().take(limit)
        }
        return Result(
            romanInput = romanInput,
            convertedText = converted,
            primaryCandidate = candidates.firstOrNull() ?: converted,
            alternatives = candidates
        )
    }
}
