package com.yash.iqtest.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.yash.iqtest.ui.theme.*
import com.yash.iqtest.viewmodel.AnswerState
import com.yash.iqtest.viewmodel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val questions by viewModel.questions
    val index     by viewModel.currentQuestionIndex
    val score     by viewModel.score
    val options   by viewModel.currentOptions
    val ansState  by viewModel.answerState
    val selected  by viewModel.selectedOption

    if (questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SageGreen)
        }
        return
    }

    val question  = questions[index]
    val total     = questions.size
    val progress  = (index.toFloat() / total)

    // Animated progress
    val animProgress by animateFloatAsState(progress, tween(600), label = "prog")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Cream, CreamDark)))
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(SageGreen.copy(alpha = 0.10f), 180.dp.toPx(), Offset(size.width, 0f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            // ── Top bar ───────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.goHome() }) {
                    Icon(Icons.Default.Close, contentDescription = "Exit", tint = EarthyBrown)
                }
                Text(
                    "${index + 1} / $total",
                    style = MaterialTheme.typography.labelLarge,
                    color = EarthyBrown
                )
                ScoreBadge(score)
            }

            Spacer(Modifier.height(12.dp))

            // ── Progress bar ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(CreamDark)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animProgress)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(listOf(SageGreen, SageGreenDark))
                        )
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Question card ─────────────────────────────────────────────────
            Card(
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier  = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    val category = viewModel.selectedCategory.value
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(category.emoji, fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            category.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = SageGreenDark
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text      = question.question,
                        style     = MaterialTheme.typography.titleLarge,
                        color     = DarkSlate,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Options ───────────────────────────────────────────────────────
            val labels = listOf("A", "B", "C", "D")
            options.forEachIndexed { i, option ->
                val isSelected  = option == selected
                val isCorrect   = option == question.correct_answer
                val bgColor = when {
                    ansState == AnswerState.NONE -> Color.White
                    isCorrect                    -> CorrectGreen.copy(alpha = 0.15f)
                    isSelected                   -> WrongRed.copy(alpha = 0.15f)
                    else                         -> Color.White
                }
                val borderColor = when {
                    ansState == AnswerState.NONE -> CreamDark
                    isCorrect                    -> CorrectGreen
                    isSelected                   -> WrongRed
                    else                         -> CreamDark
                }
                val textColor = when {
                    ansState == AnswerState.NONE -> DarkSlate
                    isCorrect                    -> CorrectGreen
                    isSelected                   -> WrongRed
                    else                         -> DarkSlate.copy(alpha = 0.45f)
                }

                OptionCard(
                    label      = labels.getOrElse(i) { "" },
                    text       = option,
                    bgColor    = bgColor,
                    borderColor= borderColor,
                    textColor  = textColor,
                    enabled    = ansState == AnswerState.NONE,
                    onClick    = { viewModel.answerQuestion(option) }
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.weight(1f))

            // ── Feedback banner ───────────────────────────────────────────────
            AnimatedVisibility(
                visible = ansState != AnswerState.NONE,
                enter   = slideInVertically { it } + fadeIn(),
                exit    = slideOutVertically { it } + fadeOut()
            ) {
                val correct = ansState == AnswerState.CORRECT
                Card(
                    shape  = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (correct) CorrectGreen else WrongRed
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(if (correct) "✓" else "✗", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (correct) "Correct! Well done!" else "Wrong! The answer was: ${question.correct_answer}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBadge(score: Int) {
    Surface(
        shape  = RoundedCornerShape(12.dp),
        color  = SageGreen.copy(alpha = 0.18f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⭐", fontSize = 14.sp)
            Spacer(Modifier.width(4.dp))
            Text("$score", style = MaterialTheme.typography.labelLarge, color = SageGreenDark)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionCard(
    label: String, text: String,
    bgColor: Color, borderColor: Color, textColor: Color,
    enabled: Boolean, onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        if (!enabled) 1f else 1f, tween(100), label = "optScale"
    )
    Card(
        onClick    = { if (enabled) onClick() },
        shape      = RoundedCornerShape(14.dp),
        colors     = CardDefaults.cardColors(containerColor = bgColor),
        border     = BorderStroke(1.5.dp, borderColor),
        elevation  = CardDefaults.cardElevation(if (enabled) 3.dp else 0.dp),
        modifier   = Modifier
            .fillMaxWidth()
            .scale(scale)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier          = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(borderColor.copy(alpha = 0.18f)),
                contentAlignment  = Alignment.Center
            ) {
                Text(label, color = borderColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text      = text,
                color     = textColor,
                style     = MaterialTheme.typography.bodyLarge,
                fontWeight= FontWeight.Medium,
                modifier  = Modifier.weight(1f)
            )
        }
    }
}
