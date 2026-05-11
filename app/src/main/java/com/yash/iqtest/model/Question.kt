package com.yash.iqtest.model

data class Question(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)