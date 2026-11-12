package com.typex.ai.keyboard

/**
 * Small offline emoji catalogue with English/Bengali keyword search.
 * Search is deterministic and does not send queries anywhere.
 */
object EmojiSearchEngine {
    data class Item(val emoji: String, val keywords: Set<String>, val category: String)

    private val items = listOf(
        Item("😀", setOf("smile","happy","হাসি","খুশি"), "smile"),
        Item("😂", setOf("laugh","tears","হাসি","মজা"), "smile"),
        Item("😊", setOf("blush","shy","সুখী","লজ্জা"), "smile"),
        Item("😍", setOf("love eyes","crush","ভালোবাসা"), "love"),
        Item("🥰", setOf("love","affection","ভালোবাসা","আদর"), "love"),
        Item("❤️", setOf("heart","love","হৃদয়","ভালোবাসা"), "love"),
        Item("😘", setOf("kiss","চুমু"), "love"),
        Item("🙏", setOf("pray","thanks","please","দোয়া","ধন্যবাদ"), "gesture"),
        Item("👍", setOf("like","good","ঠিক","ভালো"), "gesture"),
        Item("👏", setOf("clap","well done","তালি"), "gesture"),
        Item("🤝", setOf("deal","handshake","চুক্তি"), "gesture"),
        Item("💪", setOf("strong","power","শক্তি"), "gesture"),
        Item("🎉", setOf("party","celebrate","উদযাপন"), "celebrate"),
        Item("🔥", setOf("fire","hot","দারুণ","আগুন"), "celebrate"),
        Item("✨", setOf("sparkle","shine","চমৎকার"), "celebrate"),
        Item("🚀", setOf("rocket","launch","দ্রুত"), "celebrate"),
        Item("🌧️", setOf("rain","weather","বৃষ্টি"), "nature"),
        Item("🌞", setOf("sun","sunny","সূর্য"), "nature"),
        Item("🍔", setOf("food","burger","খাবার"), "food"),
        Item("☕", setOf("coffee","tea","কফি","চা"), "food"),
        Item("🐱", setOf("cat","বিড়াল"), "animals"),
        Item("🐶", setOf("dog","কুকুর"), "animals")
    )

    fun search(query: String, limit: Int = 32): List<Item> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return items.take(limit)
        return items.filter { item ->
            item.emoji == q || item.category.contains(q) ||
                item.keywords.any { it.lowercase().contains(q) }
        }.take(limit)
    }
}
