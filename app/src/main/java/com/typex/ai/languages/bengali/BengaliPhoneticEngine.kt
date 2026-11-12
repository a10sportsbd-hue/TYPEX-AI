package com.typex.ai.languages.bengali

/**
 * Offline Bengali phonetic conversion and candidate generation.
 *
 * A practical starter engine: common-word lexicon + longest-match rules.
 * It is not claimed to be a complete Avro implementation.
 */
object BengaliPhoneticEngine {

    private val dictionary = linkedMapOf(
        "ami" to "আমি", "amra" to "আমরা", "amar" to "আমার",
        "amake" to "আমাকে", "amader" to "আমাদের", "tumi" to "তুমি",
        "tomar" to "তোমার", "tomake" to "তোমাকে", "apni" to "আপনি",
        "apnader" to "আপনাদের", "bangla" to "বাংলা",
        "bangladesh" to "বাংলাদেশ", "bangladeshi" to "বাংলাদেশি",
        "bhalo" to "ভালো", "valo" to "ভালো", "kemon" to "কেমন",
        "acho" to "আছো", "achi" to "আছি", "ache" to "আছে",
        "ase" to "আছে", "hoy" to "হয়", "hobe" to "হবে",
        "korbo" to "করব", "koro" to "করো", "kori" to "করি",
        "korte" to "করতে", "kotha" to "কথা", "ki" to "কি",
        "keno" to "কেন", "khub" to "খুব", "onek" to "অনেক",
        "dhonnobad" to "ধন্যবাদ", "assalamu" to "আসসালামু",
        "alaikum" to "আলাইকুম", "salam" to "সালাম",
        "allah" to "আল্লাহ", "inshallah" to "ইনশাআল্লাহ",
        "subhanallah" to "সুবহানাল্লাহ", "shukriya" to "শুকরিয়া",
        "doya" to "দোয়া", "prem" to "প্রেম", "bhalobashi" to "ভালোবাসি",
        "jibon" to "জীবন", "manush" to "মানুষ", "shundor" to "সুন্দর",
        "sundor" to "সুন্দর", "shotti" to "সত্যি", "na" to "না",
        "hya" to "হ্যাঁ", "ha" to "হ্যাঁ", "thik" to "ঠিক",
        "naam" to "নাম", "name" to "নাম", "saif" to "সাইফ",
        "amar" to "আমার", "bari" to "বাড়ি", "bhasha" to "ভাষা",
        "desh" to "দেশ", "bondhu" to "বন্ধু", "somoy" to "সময়",
        "shokal" to "সকাল", "rat" to "রাত", "din" to "দিন",
        "khabar" to "খাবার", "pani" to "পানি", "kaj" to "কাজ",
        "school" to "স্কুল", "phone" to "ফোন", "keyboard" to "কিবোর্ড",
        "ami" to "আমি", "tahole" to "তাহলে", "ekhane" to "এখানে",
        "okhane" to "ওখানে", "kothay" to "কোথায়", "banglay" to "বাংলায়",
        "likhbo" to "লিখব", "likho" to "লিখো", "likhte" to "লিখতে",
        "porbo" to "পড়ব", "porte" to "পড়তে", "bujhi" to "বুঝি",
        "bujhte" to "বুঝতে", "shikhi" to "শিখি", "shikhbo" to "শিখব",
        "shikhte" to "শিখতে", "cholo" to "চলো", "cholun" to "চলুন",
        "ekhono" to "এখনো", "ekhon" to "এখন", "ajke" to "আজকে",
        "kalke" to "কালকে", "valo" to "ভালো", "kharap" to "খারাপ",
        "shomoy" to "সময়", "somoy" to "সময়", "proyojon" to "প্রয়োজন",
        "shob" to "সব", "sobai" to "সবাই", "kichu" to "কিছু",
        "kisu" to "কিছু", "jodi" to "যদি", "taholei" to "তাহলেই",
        "kenona" to "কেননা", "karon" to "কারণ", "jonno" to "জন্য",
        "shathe" to "সাথে", "sathe" to "সাথে", "ekhanei" to "এখানেই",
        "dhaka" to "ঢাকা", "chattogram" to "চট্টগ্রাম",
        "brahmanbaria" to "ব্রাহ্মণবাড়িয়া", "mymensingh" to "ময়মনসিংহ"
    )

    private val consonants = linkedMapOf(
        // Common conjunct clusters are single tokens: this avoids inserting a
        // virama inside the conjunct itself.
        "ngk" to "ঙ্ক", "ngg" to "ঙ্গ", "ngh" to "ঙ্ঘ",
        "nj" to "ঞ্জ", "nch" to "ঞ্চ", "ndh" to "ন্ধ",
        "nd" to "ন্দ", "nt" to "ন্ত", "nth" to "ন্থ",
        "mp" to "ম্প", "mb" to "ম্ব", "mpl" to "ম্প্ল",
        "str" to "স্ত্র", "shr" to "শ্র",
        "ksh" to "ক্ষ", "kkh" to "ক্ষ", "gy" to "জ্ঞ", "jn" to "জ্ঞ",
        "kr" to "ক্র", "khr" to "খ্র", "gr" to "গ্র", "ghr" to "ঘ্র",
        "tr" to "ত্র", "thr" to "থ্র", "dr" to "দ্র", "dhr" to "ধ্র",
        "pr" to "প্র", "phr" to "ফ্র", "br" to "ব্র", "bhr" to "ভ্র",
        "fr" to "ফ্র", "pl" to "প্ল", "bl" to "ব্ল", "kl" to "ক্ল",
        "gl" to "গ্ল", "sl" to "স্ল",
        "ng" to "ঙ", "ny" to "ঞ",
        "kh" to "খ", "gh" to "ঘ", "chh" to "ছ", "ch" to "চ",
        "jh" to "ঝ", "th" to "থ", "dh" to "ধ", "ph" to "ফ",
        "bh" to "ভ", "sh" to "শ", "ss" to "ষ", "rr" to "ড়",
        "rh" to "ঢ়", "k" to "ক", "g" to "গ", "j" to "জ",
        "t" to "ত", "d" to "দ", "n" to "ন", "p" to "প",
        "f" to "ফ", "b" to "ব", "v" to "ভ", "m" to "ম",
        "r" to "র", "l" to "ল", "s" to "স", "h" to "হ",
        "y" to "য়", "w" to "ও", "q" to "ক", "x" to "ক্স",
        "z" to "য", "c" to "চ"
    )

    private val vowels = linkedMapOf(
        "rri" to Pair("ৠ", "ৄ"),
        "oi" to Pair("ঐ", "ৈ"), "ou" to Pair("ঔ", "ৌ"),
        "aa" to Pair("আ", "া"), "ii" to Pair("ঈ", "ী"),
        "ee" to Pair("ঈ", "ী"), "uu" to Pair("ঊ", "ূ"),
        "oo" to Pair("উ", "ু"), "ri" to Pair("ঋ", "ৃ"),
        "a" to Pair("আ", "া"), "i" to Pair("ই", "ি"),
        "u" to Pair("উ", "ু"), "e" to Pair("এ", "ে"),
        "o" to Pair("ও", "ো")
    )

    fun normalizeRomanInput(input: String): String =
        input.trim().lowercase()
            .replace("ou", "ou")
            .replace("w", "o")

    fun addDictionaryEntry(roman: String, bengali: String) {
        val key = roman.trim().lowercase()
        if (key.isNotEmpty() && bengali.isNotBlank()) dictionary[key] = bengali.trim()
    }

    fun removeDictionaryEntry(roman: String) {
        dictionary.remove(roman.trim().lowercase())
    }

    fun dictionaryEntries(): Map<String, String> = dictionary.toMap()

    fun transliterate(input: String): String {
        if (input.isBlank()) return ""
        val key = input.lowercase()
        dictionary[key]?.let { return it }

        val out = StringBuilder()
        var i = 0
        var lastConsonantStart = -1
        var lastConsonantLength = 0
        var previousWasConsonant = false

        while (i < key.length) {
            val rest = key.substring(i)
            val vowelKey = vowels.keys.sortedByDescending { it.length }.firstOrNull { rest.startsWith(it) }
            if (vowelKey != null) {
                val pair = vowels.getValue(vowelKey)
                if (previousWasConsonant) out.append(pair.second) else out.append(pair.first)
                previousWasConsonant = false
                lastConsonantStart = -1
                lastConsonantLength = 0
                i += vowelKey.length
                continue
            }

            val consonantKey = consonants.keys
                .sortedByDescending { it.length }
                .firstOrNull { rest.startsWith(it) }

            if (consonantKey != null) {
                if (previousWasConsonant && lastConsonantStart >= 0) {
                    out.insert(lastConsonantStart + lastConsonantLength, "্")
                }
                val mapped = consonants.getValue(consonantKey)
                lastConsonantStart = out.length
                lastConsonantLength = mapped.length
                out.append(mapped)
                previousWasConsonant = true
                i += consonantKey.length
                continue
            }

            val ch = key[i]
            when (ch) {
                ' ' -> out.append(' ')
                '0' -> out.append('০')
                '1' -> out.append('১')
                '2' -> out.append('২')
                '3' -> out.append('৩')
                '4' -> out.append('৪')
                '5' -> out.append('৫')
                '6' -> out.append('৬')
                '7' -> out.append('৭')
                '8' -> out.append('৮')
                '9' -> out.append('৯')
                else -> out.append(ch)
            }
            previousWasConsonant = false
            lastConsonantStart = -1
            lastConsonantLength = 0
            i++
        }
        return out.toString()
    }

    fun suggestions(roman: String, limit: Int = 5): List<String> {
        val query = roman.trim().lowercase()
        if (query.isEmpty()) return emptyList()

        val exact = dictionary[query]
        val prefix = dictionary.entries
            .asSequence()
            .filter { it.key.startsWith(query) && it.key != query }
            .sortedBy { it.key.length }
            .map { it.value }
            .toList()

        val converted = transliterate(query)
        return buildList {
            if (exact != null) add(exact)
            if (converted.isNotBlank() && converted !in this) add(converted)
            prefix.forEach { if (it !in this) add(it) }
        }.take(limit)
    }

    fun romanWords(): Set<String> = dictionary.keys
}
