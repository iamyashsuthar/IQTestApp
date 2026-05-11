package com.yash.iqtest.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.text.Html
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.yash.iqtest.api.RetrofitInstance
import com.yash.iqtest.database.AppDatabase
import com.yash.iqtest.database.ScoreEntity
import com.yash.iqtest.model.ALL_CATEGORIES
import com.yash.iqtest.model.Question
import kotlinx.coroutines.launch

enum class Screen { HOME, QUIZ, RESULT }
enum class AnswerState { NONE, CORRECT, WRONG }

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application, AppDatabase::class.java, "iq_test_db"
    ).build()

    // ── Navigation ───────────────────────────────────────────────────────────
    var currentScreen = mutableStateOf(Screen.HOME)

    // ── Settings ─────────────────────────────────────────────────────────────
    var selectedCategory = mutableStateOf(ALL_CATEGORIES[0])
    var selectedDifficulty = mutableStateOf<String?>(null)
    var questionCount = mutableStateOf(10)

    // ── Quiz state ───────────────────────────────────────────────────────────
    var questions = mutableStateOf<List<Question>>(emptyList())
    var currentOptions = mutableStateOf<List<String>>(emptyList())
    var currentQuestionIndex = mutableStateOf(0)
    var score = mutableStateOf(0)
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf<String?>(null)

    // ── Answer feedback ──────────────────────────────────────────────────────
    var answerState = mutableStateOf(AnswerState.NONE)
    var selectedOption = mutableStateOf<String?>(null)

    // ── Score history ────────────────────────────────────────────────────────
    var scoreHistory = mutableStateOf<List<ScoreEntity>>(emptyList())

    init {
        loadScoreHistory()
    }

    private fun loadScoreHistory() {
        viewModelScope.launch {
            scoreHistory.value = db.scoreDao().getScores()
        }
    }

    fun startQuiz() {
        isLoading.value   = true
        loadError.value   = null
        questions.value   = emptyList()
        currentQuestionIndex.value = 0
        score.value       = 0
        answerState.value = AnswerState.NONE
        selectedOption.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getQuestions(
                    amount     = questionCount.value,
                    category   = selectedCategory.value.id,
                    difficulty = selectedDifficulty.value
                )
                questions.value = response.results.map { q ->
                    q.copy(
                        question          = decodeHtml(q.question),
                        correct_answer    = decodeHtml(q.correct_answer),
                        incorrect_answers = q.incorrect_answers.map { decodeHtml(it) }
                    )
                }
                buildOptions()
                isLoading.value = false
                currentScreen.value = Screen.QUIZ
            } catch (e: Exception) {
                isLoading.value = false
                loadError.value = "Failed to load questions. Check your internet connection."
            }
        }
    }

    private fun buildOptions() {
        val q = questions.value.getOrNull(currentQuestionIndex.value) ?: return
        currentOptions.value = (q.incorrect_answers + q.correct_answer).shuffled()
    }

    fun answerQuestion(selected: String) {
        if (answerState.value != AnswerState.NONE) return

        val q = questions.value[currentQuestionIndex.value]
        selectedOption.value = selected

        if (selected == q.correct_answer) {
            score.value++
            answerState.value = AnswerState.CORRECT
        } else {
            answerState.value = AnswerState.WRONG
        }

        Handler(Looper.getMainLooper()).postDelayed({
            advanceQuestion()
        }, 1200)
    }

    private fun advanceQuestion() {
        val next = currentQuestionIndex.value + 1
        answerState.value    = AnswerState.NONE
        selectedOption.value = null

        if (next >= questions.value.size) {
            finishQuiz()
        } else {
            currentQuestionIndex.value = next
            buildOptions()
        }
    }

    private fun finishQuiz() {
        val finalScore = score.value
        viewModelScope.launch {
            db.scoreDao().insertScore(ScoreEntity(score = finalScore))
            loadScoreHistory()
        }
        currentScreen.value = Screen.RESULT
    }

    fun goHome()    { currentScreen.value = Screen.HOME }
    fun retryQuiz() { startQuiz() }

    @Suppress("DEPRECATION")
    private fun decodeHtml(raw: String): String =
        Html.fromHtml(raw, Html.FROM_HTML_MODE_LEGACY).toString()
}
