package com.idealpestcontrol.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    @DrawableRes logoRes: Int = R.drawable.ic_logo,
    onAnimationFinished: () -> Unit = {}
) {
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color(0xFF121212) else Color.White
    val idealColor = if (darkTheme) Color(0xFF66A9DC) else Color(0xFF1E5F95)
    val pestControlColor = if (darkTheme) Color(0xFFE2676B) else Color(0xFFC52C32)
    val logoOffset = remember { Animatable(0f) }
    val brandOffset = remember { Animatable(-44f) }
    val brandAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Let the mascot sit in the exact center before revealing the brand name.
        delay(700)
        coroutineScope {
            launch {
                logoOffset.animateTo(
                    targetValue = -78f,
                    animationSpec = tween(
                        durationMillis = 850,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                brandOffset.animateTo(
                    targetValue = 25f,
                    animationSpec = tween(
                        durationMillis = 850,
                        delayMillis = 120,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            launch {
                brandAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 500,
                        delayMillis = 280
                    )
                )
            }
        }
        onAnimationFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(logoRes),
            contentDescription = stringResource(R.string.logo_content_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = logoOffset.value.dp)
                .fillMaxWidth(0.78f)
                .widthIn(max = 310.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = brandOffset.value.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .graphicsLayer { alpha = brandAlpha.value }
        ) {
            Text(
                text = stringResource(R.string.brand_ideal),
                color = idealColor,
                fontSize = 58.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                lineHeight = 58.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.brand_pest_control_services),
                color = pestControlColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.1.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        SplashScreen()
    }
}
