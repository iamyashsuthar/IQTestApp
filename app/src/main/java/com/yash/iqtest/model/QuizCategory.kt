package com.yash.iqtest.model

data class QuizCategory(
    val id: Int?,
    val name: String,
    val emoji: String
)

data class QuizSettings(
    val category: QuizCategory,
    val difficulty: String?,   // null = any, "easy", "medium", "hard"
    val amount: Int = 10
)

val ALL_CATEGORIES = listOf(
    QuizCategory(null,  "Any Category",      "🌐"),
    QuizCategory(9,     "General Knowledge", "🧠"),
    QuizCategory(17,    "Science & Nature",  "🔬"),
    QuizCategory(23,    "History",           "🏛️"),
    QuizCategory(27,    "Animals",           "🐾"),
    QuizCategory(21,    "Sports",            "⚽"),
    QuizCategory(11,    "Film",              "🎬"),
    QuizCategory(12,    "Music",             "🎵"),
    QuizCategory(15,    "Video Games",       "🎮"),
    QuizCategory(18,    "Computers",         "💻"),
    QuizCategory(22,    "Geography",         "🗺️"),
    QuizCategory(10,    "Books",             "📚"),
)

val DIFFICULTIES = listOf(
    null        to "Any",
    "easy"      to "Easy",
    "medium"    to "Medium",
    "hard"      to "Hard"
)
