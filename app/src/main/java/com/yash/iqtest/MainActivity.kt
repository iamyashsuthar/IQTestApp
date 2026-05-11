package com.yash.iqtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.yash.iqtest.ui.screens.HomeScreen
import com.yash.iqtest.ui.screens.QuizScreen
import com.yash.iqtest.ui.screens.ResultScreen
import com.yash.iqtest.ui.theme.Cream
import com.yash.iqtest.ui.theme.IQTestTheme
import com.yash.iqtest.viewmodel.QuizViewModel
import com.yash.iqtest.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IQTestTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
                    val screen = viewModel.currentScreen.value

                    AnimatedContent(
                        targetState   = screen,
                        transitionSpec = {
                            slideInHorizontally(tween(350)) { it } + fadeIn(tween(350)) togetherWith
                            slideOutHorizontally(tween(350)) { -it } + fadeOut(tween(350))
                        },
                        label = "screenAnim"
                    ) { target ->
                        when (target) {
                            Screen.HOME   -> HomeScreen(viewModel)
                            Screen.QUIZ   -> QuizScreen(viewModel)
                            Screen.RESULT -> ResultScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}
