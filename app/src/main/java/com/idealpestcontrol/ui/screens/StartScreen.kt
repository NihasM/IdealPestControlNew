package com.idealpestcontrol.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme
import kotlinx.coroutines.launch

private data class OnboardingPage(
    @param:DrawableRes val imageRes: Int,
    @param:StringRes val imageDescriptionRes: Int,
    @param:StringRes val titleRes: Int,
    @param:StringRes val bodyRes: Int,
    val overlayColor: Color
)

private val onboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.onboarding_protection,
        imageDescriptionRes = R.string.onboarding_protection_description,
        titleRes = R.string.onboarding_title_professional,
        bodyRes = R.string.onboarding_body_professional,
        overlayColor = Color(0xFF713815)
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_termite_treatment,
        imageDescriptionRes = R.string.onboarding_termite_description,
        titleRes = R.string.onboarding_title_protect_today,
        bodyRes = R.string.onboarding_body_protect_today,
        overlayColor = Color(0xFF263313)
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_home_service,
        imageDescriptionRes = R.string.onboarding_home_service_description,
        titleRes = R.string.onboarding_title_pest_free,
        bodyRes = R.string.onboarding_body_pest_free,
        overlayColor = Color(0xFF6B3517)
    )
)

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == onboardingPages.lastIndex

    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            OnboardingPageContent(page = onboardingPages[pageIndex])
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 28.dp, end = 24.dp, bottom = 28.dp)
        ) {
            PageIndicators(
                pageCount = onboardingPages.size,
                selectedPage = currentPage
            )

            Text(
                text = stringResource(if (isLastPage) R.string.start else R.string.next),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        if (isLastPage) {
                            onGetStarted()
                        } else if (!pagerState.isScrollInProgress) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = currentPage + 1,
                                    animationSpec = tween(durationMillis = 430)
                                )
                            }
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 11.dp)
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.overlayColor)
    ) {
        Image(
            painter = painterResource(page.imageRes),
            contentDescription = stringResource(page.imageDescriptionRes),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(page.overlayColor.copy(alpha = 0.12f))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color.Transparent,
                            0.43f to Color.Transparent,
                            0.67f to page.overlayColor.copy(alpha = 0.58f),
                            1.00f to page.overlayColor.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 28.dp, end = 24.dp, bottom = 116.dp)
        ) {
            Text(
                text = stringResource(page.titleRes),
                color = Color.White,
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 34.sp,
                letterSpacing = (-0.3).sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(page.bodyRes),
                color = Color.White.copy(alpha = 0.86f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 23.sp
            )
        }
    }
}

@Composable
private fun PageIndicators(
    pageCount: Int,
    selectedPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selectedPage) 10.dp else 9.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedPage) {
                            Color.White
                        } else {
                            Color.White.copy(alpha = 0.32f)
                        }
                    )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StartScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        StartScreen()
    }
}
