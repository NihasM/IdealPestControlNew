package com.idealpestcontrol.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme

private data class StartColors(
    val background: Color,
    val accent: Color,
    val title: Color,
    val body: Color,
    val actionArea: Color
)

private val LightStartColors = StartColors(
    background = Color(0xFFF9F8F7),
    accent = Color(0xFF965C4C),
    title = Color(0xFF343434),
    body = Color(0xFF4F4B49),
    actionArea = Color(0xFFF0EEEE)
)

private val DarkStartColors = StartColors(
    background = Color(0xFF121212),
    accent = Color(0xFFD8927E),
    title = Color(0xFFF3F1F0),
    body = Color(0xFFCAC6C4),
    actionArea = Color(0xFF242222)
)

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit = {}
) {
    val colors = if (isSystemInDarkTheme()) DarkStartColors else LightStartColors
    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = statusBarHeight,
                    bottom = navigationBarHeight
                )
        ) {
            StartHeading(
                colors = colors,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 42.dp, start = 32.dp, end = 32.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_termite_start),
                contentDescription = stringResource(R.string.termite_content_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 9.dp, y = 42.dp)
                    .fillMaxWidth(0.88f)
                    .padding(end = 16.dp)
            )

            GetStartedAction(
                onClick = onGetStarted,
                colors = colors,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun StartHeading(
    colors: StartColors,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.start_title_safeguarding),
            color = colors.title,
            fontSize = 29.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 34.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.start_title_home_health),
            color = colors.accent,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.start_service_message),
            color = colors.body,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GetStartedAction(
    onClick: () -> Unit,
    colors: StartColors,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(154.dp)
            .clickable(onClick = onClick)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val actionAreaPath = Path().apply {
                moveTo(0f, size.height * 0.62f)
                quadraticTo(
                    size.width * 0.5f,
                    -size.height * 0.25f,
                    size.width,
                    size.height * 0.62f
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(
                path = actionAreaPath,
                color = colors.actionArea
            )

            val borderPath = Path().apply {
                moveTo(0f, size.height * 0.62f)
                quadraticTo(
                    size.width * 0.5f,
                    -size.height * 0.25f,
                    size.width,
                    size.height * 0.62f
                )
            }
            drawPath(
                path = borderPath,
                color = colors.accent.copy(alpha = 0.72f),
                style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = stringResource(R.string.get_started),
            color = colors.accent,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 23.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StartScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        StartScreen()
    }
}
