package com.idealpestcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.idealpestcontrol.ui.screens.AddDetailsScreen
import com.idealpestcontrol.ui.screens.HomeScreen
import com.idealpestcontrol.ui.screens.ServiceDetailScreen
import com.idealpestcontrol.ui.screens.SplashScreen
import com.idealpestcontrol.ui.screens.StartScreen
import com.idealpestcontrol.ui.theme.IdealPestControlTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("ideal_pest_control_preferences", MODE_PRIVATE)
        enableEdgeToEdge()
        setContent {
            IdealPestControlTheme(dynamicColor = false) {
                var currentScreen by rememberSaveable { mutableIntStateOf(0) }
                var splashAnimationCompleted by rememberSaveable { mutableStateOf(false) }
                var profileCompleted by rememberSaveable {
                    mutableStateOf(preferences.getBoolean("profile_completed", false))
                }
                var userName by rememberSaveable {
                    mutableStateOf(preferences.getString("user_name", "").orEmpty())
                }
                var userMobile by rememberSaveable {
                    mutableStateOf(preferences.getString("user_mobile", "").orEmpty())
                }
                var userEmail by rememberSaveable {
                    mutableStateOf(preferences.getString("user_email", "").orEmpty())
                }
                var selectedDetail by rememberSaveable { mutableStateOf("") }

                SideEffect {
                    val insetsController = WindowCompat.getInsetsController(
                        window,
                        window.decorView
                    )
                    insetsController.isAppearanceLightStatusBars = true
                    insetsController.isAppearanceLightNavigationBars = true
                }

                LaunchedEffect(splashAnimationCompleted, profileCompleted) {
                    if (splashAnimationCompleted) {
                        delay(2_000)
                        currentScreen = if (profileCompleted) 3 else 1
                    }
                }

                BackHandler(enabled = currentScreen == 2 || currentScreen == 4) {
                    currentScreen = if (currentScreen == 4) 3 else 1
                }

                when (currentScreen) {
                    0 -> SplashScreen(
                        onAnimationFinished = { splashAnimationCompleted = true }
                    )

                    1 -> StartScreen(
                        onGetStarted = { currentScreen = 2 }
                    )

                    2 -> AddDetailsScreen(
                        onContinue = { name, mobile, email ->
                            userName = name
                            userMobile = mobile
                            userEmail = email
                            preferences.edit()
                                .putString("user_name", name)
                                .putString("user_mobile", mobile)
                                .putString("user_email", email)
                                .putBoolean("profile_completed", true)
                                .apply()
                            profileCompleted = true
                            currentScreen = 3
                        }
                    )

                    3 -> HomeScreen(
                        userName = userName,
                        onPestSelected = { pestName ->
                            selectedDetail = pestName
                            currentScreen = 4
                        },
                        onServiceSelected = { serviceName ->
                            selectedDetail = serviceName
                            currentScreen = 4
                        }
                    )

                    else -> ServiceDetailScreen(
                        detailKey = selectedDetail,
                        savedName = userName,
                        savedMobile = userMobile,
                        savedEmail = userEmail,
                        onBack = { currentScreen = 3 },
                        onInquiry = { name, mobile, email ->
                            userName = name
                            userMobile = mobile
                            userEmail = email
                            preferences.edit()
                                .putString("user_name", name)
                                .putString("user_mobile", mobile)
                                .putString("user_email", email)
                                .apply()
                        }
                    )
                }
            }
        }
    }
}
