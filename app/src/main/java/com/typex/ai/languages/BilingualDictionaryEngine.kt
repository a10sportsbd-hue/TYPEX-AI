package com.typex.ai.languages

data class DictionaryEntry(val english: String, val bengali: String)

object BilingualDictionaryEngine {
    private val entries = listOf(
        DictionaryEntry("hello", "হ্যালো / নমস্কার"),
        DictionaryEntry("love", "ভালোবাসা"),
        DictionaryEntry("beautiful", "সুন্দর"),
        DictionaryEntry("friend", "বন্ধু"),
        DictionaryEntry("family", "পরিবার"),
        DictionaryEntry("knowledge", "জ্ঞান"),
        DictionaryEntry("success", "সাফল্য"),
        DictionaryEntry("water", "পানি"),
        DictionaryEntry("food", "খাবার"),
        DictionaryEntry("book", "বই"),
        DictionaryEntry("language", "ভাষা"),
        DictionaryEntry("time", "সময়"),
        DictionaryEntry("work", "কাজ"),
        DictionaryEntry("happy", "খুশি"),
        DictionaryEntry("help", "সাহায্য"),
        DictionaryEntry("Thank you", "ধন্যবাদ"),
        DictionaryEntry("I", "আমি"),
        DictionaryEntry("You", "তুমি"),
        DictionaryEntry("Good / Well", "ভালো"),
        DictionaryEntry("Bangladesh", "বাংলাদেশ"),
        DictionaryEntry("Friend", "বন্ধু"),
        DictionaryEntry("Family", "পরিবার"),
        DictionaryEntry("Beautiful", "সুন্দর")
    )

    fun search(query: String, limit: Int = 20): List<DictionaryEntry> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return entries.take(limit)
        return entries.filter {
            it.english.lowercase().contains(q) || it.bengali.contains(query.trim())
        }.take(limit)
    }
}
