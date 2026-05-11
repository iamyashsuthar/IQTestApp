package com.yash.iqtest.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
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
import com.yash.iqtest.viewmodel.QuizViewModel

@Composable
fun ResultScreen(viewModel: QuizViewModel) {
    val score    by viewModel.score
    val total    = viewModel.questions.value.size
    val pct      = if (total > 0) score * 100 / total else 0

    val (emoji, label, accent) = when {
        pct >= 90 -> Triple("🏆", "Outstanding!", GoldAccent)
        pct >= 70 -> Triple("🎉", "Well Done!",   SageGreen)
        pct >= 50 -> Triple("👍", "Good Effort!", EarthyBrown)
        else      -> Triple("💪", "Keep Trying!", WrongRed)
    }

    // Animated circle progress
    val animPct by animateFloatAsState(
        targetValue    = pct / 100f,
        animationSpec  = tween(1200, easing = EaseOutCubic),
        label          = "circPct"
    )
    val animScore by animateIntAsState(score, tween(1000), label = "scoreNum")

    // Bounce emoji
    val bounce = rememberInfiniteTransition(label = "bounce")
    val bounceY by bounce.animateFloat(
        0f, -12f,
        infiniteRepeatable(tween(700, easing = EaseInOut), RepeatMode.Reverse),
        label = "bounceY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Cream, CreamDark))),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(accent.copy(alpha = 0.09f), 250.dp.toPx(), Offset(size.width / 2, size.height * 0.15f))
            drawCircle(SageGreen.copy(alpha = 0.07f), 180.dp.toPx(), Offset(size.width * 0.1f, size.height * 0.85f))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emoji
            Text(emoji, fontSize = 72.sp, modifier = Modifier.offset(y = bounceY.dp))

            Spacer(Modifier.height(16.dp))

            Text(label, style = MaterialTheme.typography.headlineLarge, color = accent, fontWeight = FontWeight.ExtraBold)

            Spacer(Modifier.height(32.dp))

            // Score ring
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                    val stroke = 14.dp.toPx()
                    drawArc(CreamDark, 0f, 360f, false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke),
                        topLeft = Offset(stroke / 2, stroke / 2),
                        size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
                    )
                    drawArc(accent, -90f, animPct * 360f, false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke,
                            cap = StrokeCap.Round),
                        topLeft = Offset(stroke / 2, stroke / 2),
                        size = androidx.compose.ui.geometry.Size(size.width - stroke, size.height - stroke)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$animScore",
                        style = MaterialTheme.typography.displayLarge,
                        color = accent,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text("/ $total", style = MaterialTheme.typography.bodyLarge, color = DarkSlate.copy(alpha = 0.6f))
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "$pct% accuracy",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkSlate.copy(alpha = 0.55f)
            )

            Spacer(Modifier.height(40.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("✅ Correct", "$score", CorrectGreen, Modifier.weight(1f))
                StatCard("❌ Wrong", "${total - score}", WrongRed, Modifier.weight(1f))
            }

            Spacer(Modifier.height(32.dp))

            // Buttons
            Button(
                onClick = { viewModel.retryQuiz() },
                shape   = RoundedCornerShape(16.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = EarthyBrown),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Play Again  🔄", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { viewModel.goHome() },
                shape   = RoundedCornerShape(16.dp),
                border  = BorderStroke(1.5.dp, SageGreen),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Change Settings  ⚙️", color = SageGreenDark, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = color, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium, color = DarkSlate.copy(alpha = 0.7f), textAlign = TextAlign.Center)
        }
    }
}
