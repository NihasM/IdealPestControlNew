package com.idealpestcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.idealpestcontrol.ui.screens.LoginSignUpScreen
import com.idealpestcontrol.ui.screens.SplashScreen
import com.idealpestcontrol.ui.screens.StartScreen
import com.idealpestcontrol.ui.theme.IdealPestControlTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IdealPestControlTheme(dynamicColor = false) {
                var currentScreen by rememberSaveable { mutableIntStateOf(0) }
                var splashAnimationCompleted by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(splashAnimationCompleted) {
                    if (splashAnimationCompleted) {
                        delay(2_000)
                        currentScreen = 1
                    }
                }

                BackHandler(enabled = currentScreen == 2) {
                    currentScreen = 1
                }

                when (currentScreen) {
                    0 -> SplashScreen(
                        onAnimationFinished = { splashAnimationCompleted = true }
                    )

                    1 -> StartScreen(
                        onGetStarted = { currentScreen = 2 }
                    )

                    else -> LoginSignUpScreen()
                }
            }
        }
    }
}
