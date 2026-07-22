package com.idealpestcontrol.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Immutable
data class BannerItem(
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val contentDescription: String,
    val actionLabel: String? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun InfiniteBannerCarousel(
    banners: List<BannerItem>,
    @DrawableRes placeholderRes: Int,
    modifier: Modifier = Modifier,
    autoScrollIntervalMillis: Long = 3_000L
) {
    if (banners.isEmpty()) return

    val isInfinite = banners.size > 1
    val pageCount = if (isInfinite) Int.MAX_VALUE else 1
    val initialPage = remember(banners.size) {
        if (!isInfinite) 0 else {
            val middle = Int.MAX_VALUE / 2
            middle - middle % banners.size
        }
    }
    // rememberPagerState uses saveable state, retaining the current page across recreation.
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { pageCount }
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState, banners.size, isDragged, autoScrollIntervalMillis) {
        if (isInfinite && !isDragged) {
            while (true) {
                delay(autoScrollIntervalMillis.coerceAtLeast(1_000L))
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = 650)
                )
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 44.dp, vertical = 10.dp),
            pageSpacing = 12.dp,
            userScrollEnabled = isInfinite,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        ) { virtualPage ->
            val banner = banners[virtualPage % banners.size]
            val pageOffset = (
                (pagerState.currentPage - virtualPage) + pagerState.currentPageOffsetFraction
            ).coerceIn(-1f, 1f)
            val distanceFromCenter = pageOffset.absoluteValue
            val centerProgress = 1f - distanceFromCenter

            BannerPage(
                banner = banner,
                placeholderRes = placeholderRes,
                pageOffset = pageOffset,
                centerProgress = centerProgress
            )
        }

        Spacer(Modifier.height(4.dp))
        CarouselIndicators(
            pageCount = banners.size,
            selectedPage = pagerState.currentPage % banners.size,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun BannerPage(
    banner: BannerItem,
    @DrawableRes placeholderRes: Int,
    pageOffset: Float,
    centerProgress: Float
) {
    val scale = 0.90f + (0.10f * centerProgress)
    val opacity = 0.62f + (0.38f * centerProgress)
    val cornerRadius = (20f + (10f * centerProgress)).dp
    val elevation = (2f + (12f * centerProgress)).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = opacity
                rotationZ = pageOffset * -2.5f
                shadowElevation = elevation.toPx()
                shape = RoundedCornerShape(cornerRadius)
                clip = true
            }
            .background(Color(0xFFE8DED4))
            .clickable(
                onClickLabel = "Open ${banner.title}",
                onClick = banner.onClick
            )
    ) {
        AsyncImage(
            model = banner.imageUrl,
            contentDescription = banner.contentDescription,
            placeholder = painterResource(placeholderRes),
            error = painterResource(placeholderRes),
            fallback = painterResource(placeholderRes),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.14f
                    scaleY = 1.14f
                    translationX = pageOffset * size.width * 0.11f
                }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.42f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.84f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Text(
                text = banner.title,
                color = Color.White,
                fontSize = 22.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = banner.subtitle,
                color = Color.White.copy(alpha = 0.86f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            banner.actionLabel?.let { actionLabel ->
                Spacer(Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.94f), CircleShape)
                        .clickable(
                            onClickLabel = actionLabel,
                            onClick = banner.onClick
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = actionLabel,
                        color = Color(0xFF713515),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("→", color = Color(0xFF713515), fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun CarouselIndicators(
    pageCount: Int,
    selectedPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(12.dp)
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == selectedPage
            val width by animateDpAsState(
                targetValue = if (isSelected) 20.dp else 7.dp,
                animationSpec = tween(durationMillis = 280),
                label = "carousel indicator width"
            )
            val color by animateColorAsState(
                targetValue = if (isSelected) Color(0xFFB85F2E) else Color(0xFFD8C9BE),
                animationSpec = tween(durationMillis = 280),
                label = "carousel indicator color"
            )
            Box(
                Modifier
                    .width(width)
                    .height(7.dp)
                    .background(color, CircleShape)
            )
        }
    }
}
