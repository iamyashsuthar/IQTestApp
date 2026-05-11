package com.yash.iqtest.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import com.yash.iqtest.model.*
import com.yash.iqtest.ui.theme.*
import com.yash.iqtest.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: QuizViewModel) {
    val scrollState = rememberScrollState()
    val selectedCategory by viewModel.selectedCategory
    val selectedDifficulty by viewModel.selectedDifficulty
    val isLoading by viewModel.isLoading
    val error by viewModel.loadError
    val history by viewModel.scoreHistory

    // Pulse animation for the brain emoji
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val scale by pulseAnim.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Cream, CreamDark)
                )
            )
    ) {
        // Decorative circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SageGreen.copy(alpha = 0.12f), 220.dp.toPx(), Offset(size.width * 0.85f, 80.dp.toPx()))
            drawCircle(EarthyBrown.copy(alpha = 0.07f), 160.dp.toPx(), Offset(30.dp.toPx(), size.height * 0.7f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero ──────────────────────────────────────────────────────────
            Text("🧠", fontSize = 72.sp, modifier = Modifier.scale(scale))
            Spacer(Modifier.height(12.dp))
            Text(
                "IQ Challenge",
                style = MaterialTheme.typography.displayLarge,
                color = EarthyBrown,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Test your knowledge",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkSlate.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(32.dp))

            // ── Category picker ───────────────────────────────────────────────
            SectionLabel("Select Category")
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 480.dp)
            ) {
                items(ALL_CATEGORIES) { cat ->
                    CategoryChip(
                        category = cat,
                        selected = cat == selectedCategory,
                        onClick  = { viewModel.selectedCategory.value = cat }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Difficulty picker ─────────────────────────────────────────────
            SectionLabel("Difficulty")
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DIFFICULTIES.forEach { (diff, label) ->
                    val selected = selectedDifficulty == diff
                    FilterChip(
                        selected = selected,
                        onClick  = { viewModel.selectedDifficulty.value = diff },
                        label    = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor    = SageGreen,
                            selectedLabelColor        = Color.White,
                            containerColor            = CreamDark,
                            labelColor                = DarkSlate
                        )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Error banner ──────────────────────────────────────────────────
            AnimatedVisibility(visible = error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = WrongRed.copy(alpha = 0.12f)),
                    shape  = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        error ?: "",
                        color = WrongRed,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Start button ──────────────────────────────────────────────────
            Button(
                onClick   = { viewModel.startQuiz() },
                enabled   = !isLoading,
                shape     = RoundedCornerShape(16.dp),
                colors    = ButtonDefaults.buttonColors(
                    containerColor = EarthyBrown,
                    contentColor   = Color.White
                ),
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(12.dp))
                    Text("Loading…", style = MaterialTheme.typography.labelLarge, color = Color.White)
                } else {
                    Text("Start Quiz  🚀", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp, color = Color.White)
                }
            }

            // ── Score history ─────────────────────────────────────────────────
            if (history.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                SectionLabel("Past Scores")
                Spacer(Modifier.height(10.dp))
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        history.takeLast(5).reversed().forEachIndexed { i, s ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("#${i + 1}", color = EarthyBrown, fontWeight = FontWeight.SemiBold)
                                Text("${s.score} / ${questionCount} pts", color = DarkSlate)
                                val medal = when (i) { 0 -> "🥇"; 1 -> "🥈"; 2 -> "🥉"; else -> "  " }
                                Text(medal)
                            }
                            if (i < minOf(history.size, 5) - 1)
                                Divider(color = CreamDark, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge,
        color = EarthyBrown,
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryChip(
    category: QuizCategory,
    selected: Boolean,
    onClick:  () -> Unit
) {
    val bg = if (selected) SageGreen else Color.White
    val border = if (selected) BorderStroke(0.dp, Color.Transparent)
                 else BorderStroke(1.dp, CreamDark)
    Card(
        onClick   = onClick,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = bg),
        border    = border,
        elevation = CardDefaults.cardElevation(if (selected) 4.dp else 1.dp),
        modifier  = Modifier.aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(category.emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                category.name,
                style     = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
                color     = if (selected) Color.White else DarkSlate,
                textAlign = TextAlign.Center,
                maxLines  = 2
            )
        }
    }
}

// Expose questionCount for history display
private val questionCount = 10
