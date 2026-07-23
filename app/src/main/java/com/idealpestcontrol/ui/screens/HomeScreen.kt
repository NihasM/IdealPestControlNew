package com.idealpestcontrol.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme

private val Espresso = Color(0xFF3A241C)
private val Cocoa = Color(0xFF8D451E)
private val Cream = Color(0xFFFFFBF6)
private val MutedBrown = Color(0xFF79685E)

private data class Pest(val name: String, val imageRes: Int, val color: Color)

private val pests = listOf(
    Pest("Termites", R.drawable.pest_termite, Color(0xFFF8ECDD)),
    Pest("Cockroaches", R.drawable.pest_cockroach, Color(0xFFFFE8D8)),
    Pest("Rodents", R.drawable.pest_rodents, Color(0xFFF1E8E4)),
    Pest("Bed Bugs", R.drawable.pest_bed_bugs, Color(0xFFF1E4DF)),
    Pest("Mosquitoes", R.drawable.pest_mosquito, Color(0xFFE7F0E9)),
    Pest("Rodents", R.drawable.pest_rodents, Color(0xFFF1E8E4)),
    Pest("Ants", R.drawable.pest_ants, Color(0xFFF4EBDD)),

)

private data class Service(
    val title: String,
    val description: String,
    val type: ServiceIcon,
    val tint: Color,
    val imageRes: Int? = null
)

private enum class ServiceIcon { Home, Building, Office, Industrial }

private val services = listOf(
    Service("Home Pest Control", "Complete protection for every corner of your home", ServiceIcon.Home, Color(0xFFF2E3CD), R.drawable.service_house),
    Service("Building Protection", "Reliable plans for apartments and shared spaces", ServiceIcon.Building, Color(0xFFE4E9E6), R.drawable.service_building),
    Service("Office Pest Control", "Quiet treatments with zero workday disruption", ServiceIcon.Office, Color(0xFFECE5DF), R.drawable.service_office),
    Service("Industrial Pest Control", "Specialist protection for factories and industrial sites", ServiceIcon.Industrial, Color(0xFFE7E2DB), R.drawable.service_industrial)
)

internal val homeHeroSlides = listOf(
    BannerItem(
        imageUrl = "https://images.unsplash.com/photo-1581578731548-c64695cc6952?auto=format&fit=crop&w=1400&q=85",
        title = "A pest-free home is a happy home",
        subtitle = "Safe, reliable care for healthier everyday living.",
        contentDescription = "A professional carefully cleaning and protecting a home"
    ),
    BannerItem(
        imageUrl = "https://images.unsplash.com/photo-1563453392212-326f5e854473?auto=format&fit=crop&w=1400&q=85",
        title = "Expert protection for every space",
        subtitle = "Reliable treatment with lasting peace of mind.",
        contentDescription = "Professional home-care supplies prepared for service"
    ),
    BannerItem(
        imageUrl = "https://images.unsplash.com/photo-1556911220-bff31c812dba?auto=format&fit=crop&w=1400&q=85",
        title = "Keep unwanted guests away",
        subtitle = "Professional pest care whenever you need it.",
        contentDescription = "A bright, clean and comfortable modern kitchen"
    )
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String = "there",
    onPestSelected: (String) -> Unit = {},
    onServiceSelected: (String) -> Unit = {}
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var selectedNavigation by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 18.dp,
                bottom = 126.dp + navigationBarPadding
            )
        ) {
            item { HomeHeader(userName = userName) }
            item { HeroCarousel() }
            item { PestSlider(onPestSelected = onPestSelected) }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    OfferCard()
                }
            }
            item { ServicesSection(onServiceSelected = onServiceSelected) }
        }

        GlassBottomNavigation(
            selectedIndex = selectedNavigation,
            onSelected = { selectedNavigation = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 18.dp, end = 18.dp, bottom = 14.dp + navigationBarPadding)
        )
    }
}

@Composable
private fun HomeHeader(userName: String) {
    val greetingName = userName.trim().substringBefore(' ').ifBlank { "there" }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(start = 12.dp)) {
            DefaultProfileIcon()
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hello, $greetingName!",
                color = Espresso,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Let’s make your home pest-free",
                color = MutedBrown,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Ideal Pest Control app icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(58.dp)
        )
    }
}

@Composable
private fun DefaultProfileIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(54.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.94f))
            .border(1.dp, Color(0xFFE7D7C8), CircleShape)
    ) {
        Canvas(Modifier.size(33.dp)) {
            drawCircle(
                color = Cocoa,
                radius = size.minDimension * 0.19f,
                center = Offset(size.width / 2f, size.height * 0.31f)
            )
            drawArc(
                color = Cocoa,
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = true,
                topLeft = Offset(size.width * 0.15f, size.height * 0.49f),
                size = Size(size.width * 0.7f, size.height * 0.52f)
            )
        }
    }
}

@Composable
private fun HeroCarousel() {
    InfiniteBannerCarousel(
        banners = homeHeroSlides,
        placeholderRes = R.drawable.home_hero_pest_control
    )
}

@Composable
private fun PestSlider(onPestSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
        Text(
            text = "What’s bothering you?",
            color = Espresso,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
        ) {
            items(pests) { pest ->
                PestItem(
                    pest = pest,
                    onClick = { onPestSelected(pest.name) }
                )
            }
        }
    }
}

@Composable
private fun PestItem(pest: Pest, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(78.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                onClickLabel = "View ${pest.name} details",
                onClick = onClick
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(pest.color)
        ) {
            Image(
                painter = painterResource(pest.imageRes),
                contentDescription = pest.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(7.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = pest.name,
            color = Espresso,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun OfferCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFB56D36), Color(0xFF713515))))
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(68.dp)
                .border(1.dp, Color.White.copy(alpha = 0.75f), CircleShape)
                .padding(5.dp)
                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
        ) {
            Text("20%\nOFF", color = Color.White, fontSize = 16.sp, lineHeight = 17.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Summer special", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Text("Save on all services this week", color = Color.White.copy(alpha = 0.82f), fontSize = 12.sp)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.95f))
                .clickable { }
        ) {
            Text("→", color = Cocoa, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ServicesSection(onServiceSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
        Text(
            text = "Our Services",
            color = Espresso,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
        ) {
            items(services) { service ->
                ServiceCard(
                    service = service,
                    cardWidth = 190.dp,
                    onClick = { onServiceSelected(service.title) }
                )
            }
        }
    }
}

@Composable
private fun ServiceCard(service: Service, cardWidth: Dp, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(cardWidth)
            .height(230.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp), ambientColor = Cocoa.copy(alpha = 0.12f))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(
                onClickLabel = "View ${service.title} details",
                onClick = onClick
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .background(
                    Brush.linearGradient(
                        listOf(service.tint.copy(alpha = 0.72f), service.tint)
                    )
                )
        ) {
            service.imageRes?.let { imageRes ->
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = service.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .scale(1.15f)
                )
            } ?: ServiceIllustration(service.type, Modifier.size(58.dp))
        }
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 9.dp)) {
            Column(modifier = Modifier.padding(end = 2.dp)) {
                Text(
                    text = service.title,
                    color = Espresso,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = service.description,
                    color = MutedBrown,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF8F5F1))
            ) {
                Text("→", color = Cocoa, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ServiceIllustration(type: ServiceIcon, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round)
        val color = Cocoa
        when (type) {
            ServiceIcon.Home -> {
                val roof = Path().apply {
                    moveTo(size.width * 0.15f, size.height * 0.48f)
                    lineTo(size.width * 0.5f, size.height * 0.2f)
                    lineTo(size.width * 0.85f, size.height * 0.48f)
                }
                drawPath(roof, color, style = stroke)
                drawRect(color, Offset(size.width * 0.24f, size.height * 0.45f), Size(size.width * 0.52f, size.height * 0.36f), style = stroke)
                drawRect(color, Offset(size.width * 0.44f, size.height * 0.59f), Size(size.width * 0.15f, size.height * 0.22f), style = stroke)
            }
            ServiceIcon.Building -> {
                drawRoundRect(color, Offset(size.width * 0.24f, size.height * 0.17f), Size(size.width * 0.52f, size.height * 0.66f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx()), style = stroke)
                repeat(3) { row ->
                    repeat(2) { column ->
                        drawCircle(color, 2.8.dp.toPx(), Offset(size.width * (0.39f + column * 0.22f), size.height * (0.32f + row * 0.16f)))
                    }
                }
            }
            ServiceIcon.Office -> {
                drawRoundRect(color, Offset(size.width * 0.18f, size.height * 0.32f), Size(size.width * 0.64f, size.height * 0.45f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(7.dp.toPx()), style = stroke)
                drawLine(color, Offset(size.width * 0.18f, size.height * 0.5f), Offset(size.width * 0.82f, size.height * 0.5f), strokeWidth = 3.2.dp.toPx())
                drawRoundRect(color, Offset(size.width * 0.38f, size.height * 0.2f), Size(size.width * 0.24f, size.height * 0.19f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5.dp.toPx()), style = stroke)
            }
            ServiceIcon.Industrial -> {
                drawRect(color, Offset(size.width * 0.17f, size.height * 0.48f), Size(size.width * 0.66f, size.height * 0.32f), style = stroke)
                drawLine(color, Offset(size.width * 0.17f, size.height * 0.48f), Offset(size.width * 0.37f, size.height * 0.34f), strokeWidth = 3.2.dp.toPx())
                drawLine(color, Offset(size.width * 0.37f, size.height * 0.34f), Offset(size.width * 0.53f, size.height * 0.48f), strokeWidth = 3.2.dp.toPx())
                drawRect(color, Offset(size.width * 0.64f, size.height * 0.18f), Size(size.width * 0.12f, size.height * 0.3f), style = stroke)
            }
        }
    }
}

private data class NavDestination(val label: String, val icon: NavIcon)
private enum class NavIcon { Home, Calendar, Info }

private val navigationItems = listOf(
    NavDestination("Home", NavIcon.Home),
    NavDestination("Bookings", NavIcon.Calendar),
    NavDestination("About", NavIcon.Info)
)

@Composable
private fun GlassBottomNavigation(
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(22.dp, RoundedCornerShape(30.dp), ambientColor = Espresso.copy(alpha = 0.22f))
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White.copy(alpha = 0.88f))
            .border(1.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(30.dp))
            .padding(6.dp)
    ) {
        val itemWidth = maxWidth / navigationItems.size
        val indicatorOffset by animateDpAsState(
            targetValue = itemWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.72f, stiffness = 420f),
            label = "glass navigation slider"
        )
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(itemWidth)
                .fillMaxSize()
                .clip(RoundedCornerShape(25.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Cocoa.copy(alpha = 0.94f), Color(0xFF6F3116).copy(alpha = 0.96f))
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(25.dp))
        )
        Row(modifier = Modifier.fillMaxSize()) {
            navigationItems.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .width(itemWidth)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(25.dp))
                        .clickable { onSelected(index) }
                ) {
                    NavigationIcon(item.icon, if (selected) Color.White else MutedBrown)
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = item.label,
                        color = if (selected) Color.White else MutedBrown,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(type: NavIcon, color: Color) {
    Canvas(Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.2.dp.toPx(), cap = StrokeCap.Round)
        when (type) {
            NavIcon.Home -> {
                val path = Path().apply {
                    moveTo(size.width * 0.16f, size.height * 0.48f)
                    lineTo(size.width * 0.5f, size.height * 0.18f)
                    lineTo(size.width * 0.84f, size.height * 0.48f)
                    lineTo(size.width * 0.76f, size.height * 0.48f)
                    lineTo(size.width * 0.76f, size.height * 0.82f)
                    lineTo(size.width * 0.24f, size.height * 0.82f)
                    lineTo(size.width * 0.24f, size.height * 0.48f)
                    close()
                }
                drawPath(path, color, style = stroke)
            }
            NavIcon.Calendar -> {
                drawRoundRect(color, Offset(size.width * 0.15f, size.height * 0.24f), Size(size.width * 0.7f, size.height * 0.62f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()), style = stroke)
                drawLine(color, Offset(size.width * 0.15f, size.height * 0.43f), Offset(size.width * 0.85f, size.height * 0.43f), strokeWidth = 2.2.dp.toPx())
                drawLine(color, Offset(size.width * 0.34f, size.height * 0.14f), Offset(size.width * 0.34f, size.height * 0.31f), strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.66f, size.height * 0.14f), Offset(size.width * 0.66f, size.height * 0.31f), strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
            }
            NavIcon.Info -> {
                drawCircle(color, size.minDimension * 0.36f, center, style = stroke)
                drawCircle(color, 1.5.dp.toPx(), Offset(size.width / 2f, size.height * 0.33f))
                drawLine(color, Offset(size.width / 2f, size.height * 0.47f), Offset(size.width / 2f, size.height * 0.69f), strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        HomeScreen()
    }
}
