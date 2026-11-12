package com.typex.ai.languages

/**
 * Phase 23 offline English typing assistant.
 *
 * Provides context-ranked next-word predictions, conservative spelling
 * alternatives, contractions and sentence-case helpers. It never commits or
 * replaces text automatically. It uses a local starter lexicon and optional
 * user frequency scores supplied by the caller.
 */
class AdvancedEnglishTypingEngine(
    private val lexicon: Set<String> = WordSuggestionEngine.DEFAULT_WORDS + EXTRA_WORDS,
    private val frequency: Map<String, Int> = emptyMap()
) {
    data class Analysis(
        val normalizedToken: String,
        val predictions: List<String>,
        val correction: String?,
        val expandedContraction: String?
    )

    private val context = mapOf(
        "how" to listOf("are", "is", "can", "do"),
        "how are" to listOf("you", "we", "they"),
        "thank" to listOf("you"),
        "good" to listOf("morning", "afternoon", "evening", "night"),
        "i" to listOf("am", "will", "can", "would", "need", "want", "think"),
        "i am" to listOf("fine", "happy", "sorry", "ready", "here"),
        "i will" to listOf("be", "try", "help", "send", "call"),
        "please" to listOf("help", "send", "check", "let", "confirm"),
        "see" to listOf("you"),
        "see you" to listOf("soon", "tomorrow", "later"),
        "let" to listOf("me", "us"),
        "let me" to listOf("know", "see", "check", "help"),
        "what" to listOf("is", "are", "do", "about"),
        "where" to listOf("are", "is", "can"),
        "are" to listOf("you", "we", "they"),
        "you" to listOf("are", "can", "will", "have", "should")
    )

    private val contractions = mapOf(
        "im" to "I'm", "dont" to "don't", "cant" to "can't",
        "wont" to "won't", "isnt" to "isn't", "arent" to "aren't",
        "wasnt" to "wasn't", "werent" to "weren't", "didnt" to "didn't",
        "doesnt" to "doesn't", "couldnt" to "couldn't", "wouldnt" to "wouldn't",
        "shouldnt" to "shouldn't", "ive" to "I've", "youre" to "you're",
        "theyre" to "they're", "were" to "we're", "weve" to "we've",
        "youve" to "you've", "ill" to "I'll", "youll" to "you'll",
        "theyve" to "they've", "thats" to "that's", "whats" to "what's",
        "lets" to "let's", "hes" to "he's", "shes" to "she's"
    )

    fun analyze(
        token: String,
        previousWords: List<String> = emptyList(),
        limit: Int = 5
    ): Analysis {
        val clean = token.trim().lowercase()
        val contextKey = previousWords.takeLast(2).joinToString(" ") { it.trim().lowercase() }
        val contextual = context[contextKey].orEmpty()
        val oneWordContext = previousWords.lastOrNull()?.trim()?.lowercase().orEmpty()
        val predictions = (contextual + context[oneWordContext].orEmpty() +
            prefixPredictions(clean))
            .distinct()
            .sortedWith(compareByDescending<String> { frequency[it] ?: 0 }
                .thenBy { it.length }.thenBy { it })
            .take(limit.coerceAtLeast(0))
        val correction = spellingCandidate(clean)
        return Analysis(
            normalizedToken = clean,
            predictions = predictions,
            correction = correction,
            expandedContraction = contractions[clean]
        )
    }

    fun predictNext(previousWords: List<String>, limit: Int = 4): List<String> {
        if (limit <= 0 || previousWords.isEmpty()) return emptyList()
        val key = previousWords.takeLast(2).joinToString(" ") { it.trim().lowercase() }
        val last = previousWords.last().trim().lowercase()
        return (context[key].orEmpty() + context[last].orEmpty())
            .distinct()
            .sortedWith(compareByDescending<String> { frequency[it] ?: 0 }
                .thenBy { it.length })
            .take(limit)
    }

    fun spellingCandidate(token: String): String? {
        val q = token.trim().lowercase()
        if (q.length < 4 || q in lexicon || q in contractions) return null
        val maxDistance = if (q.length >= 8) 2 else 1
        return lexicon.asSequence()
            .map { it to editDistance(q, it) }
            .filter { (word, distance) ->
                distance in 1..maxDistance &&
                    (word.firstOrNull() == q.firstOrNull() ||
                        q.length >= 7 && word.take(2) == q.take(2))
            }
            .sortedWith(compareBy<Pair<String, Int>> { it.second }
                .thenByDescending { isSubsequence(q, it.first) }
                .thenByDescending { frequency[it.first] ?: 0 }
                .thenBy { it.first.length }.thenBy { it.first })
            .firstOrNull()?.first
    }

    fun expandContraction(token: String): String? =
        contractions[token.trim().lowercase()]

    fun sentenceCase(text: String, sentenceStart: Boolean = true): String {
        if (!sentenceStart || text.isEmpty()) return text
        val first = text.indexOfFirst { it.isLetter() }
        if (first < 0) return text
        return text.substring(0, first) +
            text[first].uppercaseChar() + text.substring(first + 1)
    }

    private fun isSubsequence(needle: String, haystack: String): Boolean {
        var index = 0
        for (character in haystack) {
            if (index < needle.length && needle[index] == character) index++
        }
        return index == needle.length
    }

    private fun prefixPredictions(prefix: String): List<String> {
        if (prefix.isEmpty()) return emptyList()
        return lexicon.asSequence()
            .filter { it.startsWith(prefix) && it != prefix }
            .sortedWith(compareByDescending<String> { frequency[it] ?: 0 }
                .thenBy { it.length }.thenBy { it })
            .take(8).toList()
    }

    private fun editDistance(a: String, b: String): Int {
        if (kotlin.math.abs(a.length - b.length) > 2) return 3
        var previous = IntArray(b.length + 1) { it }
        for (i in a.indices) {
            val current = IntArray(b.length + 1)
            current[0] = i + 1
            for (j in b.indices) current[j + 1] = minOf(
                current[j] + 1,
                previous[j + 1] + 1,
                previous[j] + if (a[i] == b[j]) 0 else 1
            )
            previous = current
        }
        return previous[b.length]
    }

    companion object {
        val EXTRA_WORDS = setOf(
            "ability", "account", "actually", "address", "amazing", "android",
            "answer", "application", "available", "beautiful", "better",
            "business", "camera", "change", "computer", "content", "creative",
            "development", "different", "digital", "everything", "experience",
            "family", "favorite", "friendship", "future", "information",
            "keyboard", "language", "learning", "message", "mobile", "morning",
            "natural", "people", "prediction", "project", "quality", "question",
            "remember", "service", "something", "support", "technology",
            "typing", "understand", "welcome", "without", "wonderful"
        )
    }
}
