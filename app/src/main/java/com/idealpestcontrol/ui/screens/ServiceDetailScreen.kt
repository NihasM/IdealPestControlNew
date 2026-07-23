package com.idealpestcontrol.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme

private val DetailEspresso = Color(0xFF3A241C)
private val DetailCocoa = Color(0xFF8D451E)
private val DetailCream = Color(0xFFFFFBF6)
private val DetailMuted = Color(0xFF79685E)
private val DetailBorder = Color(0xFFEBDDD2)

private data class DetailContent(
    val title: String,
    val introduction: String,
    val problems: List<String>,
    val solutionTitle: String,
    val solutions: List<String>
)

private val detailContent = mapOf(
    "Ants" to DetailContent(
        title = "Ant",
        introduction = "Ants live in organized colonies and enter buildings through tiny gaps while searching for food, water and shelter. A visible trail often means a nest or dependable food source is nearby.",
        problems = listOf(
            "Contaminate food, preparation areas and kitchen surfaces.",
            "Build colonies in walls, floors, gardens and electrical points.",
            "Some species bite or sting and can irritate the skin.",
            "Return quickly when the nest and entry routes are not treated."
        ),
        solutionTitle = "How We Eliminate Ants",
        solutions = listOf(
            "Inspect trails, entry points and likely nesting areas.",
            "Identify the ant species so the correct treatment is selected.",
            "Use targeted baits and safe treatments that reach the colony.",
            "Seal practical entry points and advise on long-term prevention."
        )
    ),
    "Cockroaches" to DetailContent(
        title = "Cockroach",
        introduction = "Cockroaches hide in warm, dark and damp spaces near food and water. They reproduce quickly, are mostly active at night and can survive in narrow cracks that ordinary cleaning cannot reach.",
        problems = listOf(
            "Carry bacteria onto food, utensils and preparation surfaces.",
            "Leave droppings, egg cases, stains and unpleasant odours.",
            "Shed particles that may aggravate allergies and asthma.",
            "Multiply rapidly when hidden harbourages are left untreated."
        ),
        solutionTitle = "How We Eliminate Cockroaches",
        solutions = listOf(
            "Inspect kitchens, drains, appliances and hidden harbourages.",
            "Apply precise gel baits and crack-and-crevice treatments.",
            "Treat breeding zones while protecting people, pets and food areas.",
            "Provide sanitation and proofing guidance to prevent reinfestation."
        )
    ),
    "Mosquitoes" to DetailContent(
        title = "Mosquito",
        introduction = "Mosquitoes breed in standing water and rest in shaded, humid areas. Even small amounts of water in planters, drains or containers can support a new generation close to your home or workplace.",
        problems = listOf(
            "Cause persistent, itchy bites and disturb sleep and outdoor activity.",
            "Can transmit serious mosquito-borne diseases.",
            "Breed quickly in overlooked water-holding containers and drains.",
            "Reduce comfort in gardens, balconies and common areas."
        ),
        solutionTitle = "How We Control Mosquitoes",
        solutions = listOf(
            "Survey the property for breeding water and adult resting areas.",
            "Treat appropriate drains, vegetation and high-risk zones.",
            "Remove or manage standing water wherever practical.",
            "Recommend scheduled monitoring during peak mosquito seasons."
        )
    ),
    "Rodents" to DetailContent(
        title = "Rodent",
        introduction = "Rats and mice enter through surprisingly small openings in search of food and safe nesting sites. Scratching sounds, droppings, gnaw marks and damaged packaging are common warning signs.",
        problems = listOf(
            "Contaminate stored food and surfaces with urine and droppings.",
            "Gnaw cables, pipes, insulation, furniture and building materials.",
            "Carry parasites and organisms associated with disease.",
            "Create fire, equipment and structural risks when activity continues."
        ),
        solutionTitle = "How We Control Rodents",
        solutions = listOf(
            "Inspect for droppings, runs, nesting sites and access points.",
            "Create a monitored trapping or baiting plan for the property.",
            "Recommend proofing for gaps, vents, doors and service openings.",
            "Track activity and adjust controls until the infestation is resolved."
        )
    ),
    "Termites" to DetailContent(
        title = "Termite",
        introduction = "Termites feed on cellulose and often damage timber from the inside, so an infestation may remain hidden for a long time. Mud tubes, hollow timber and discarded wings can indicate active termites.",
        problems = listOf(
            "Weaken doors, frames, furniture, flooring and structural timber.",
            "Cause expensive damage before obvious signs become visible.",
            "Travel through concealed soil routes, cracks and wall cavities.",
            "Reinfest when the colony and building conditions are not addressed."
        ),
        solutionTitle = "How We Eliminate Termites",
        solutions = listOf(
            "Perform a detailed inspection and assess the extent of activity.",
            "Select an appropriate soil, barrier, baiting or wood treatment.",
            "Treat active zones and vulnerable concealed entry routes.",
            "Provide monitoring and prevention advice for lasting protection."
        )
    ),
    "Bed Bugs" to DetailContent(
        title = "Bed Bug",
        introduction = "Bed bugs hide close to sleeping and resting areas in mattress seams, furniture joints, skirting and small cracks. They can travel on luggage, clothing and used furniture and are difficult to remove without a complete treatment.",
        problems = listOf(
            "Feed at night and can cause itchy bites and loss of sleep.",
            "Spread between rooms through belongings and furniture.",
            "Hide in very small gaps where household sprays do not reach.",
            "Survive for long periods and return if eggs or hiding sites remain."
        ),
        solutionTitle = "How We Eliminate Bed Bugs",
        solutions = listOf(
            "Inspect beds, furniture, edges and adjoining hiding places.",
            "Use a coordinated treatment suited to the level of infestation.",
            "Target adults, nymphs and concealed harbourages thoroughly.",
            "Explain preparation, follow-up and prevention for reliable control."
        )
    ),
    "Home Pest Control" to DetailContent(
        title = "Home Pest Control",
        introduction = "A home protection plan controls common pests while respecting your family, pets and daily routine. Treatment is tailored to the rooms, pest activity and conditions found during inspection.",
        problems = listOf(
            "Pests can contaminate kitchens, stores and living spaces.",
            "Hidden nests and entry points allow recurring infestations.",
            "DIY sprays may scatter activity without treating the source.",
            "Different pests require different methods and safety precautions."
        ),
        solutionTitle = "What Our Home Service Includes",
        solutions = listOf(
            "A room-by-room inspection and clear treatment recommendation.",
            "Targeted treatment for the pests and risk areas we identify.",
            "Careful application around children, pets and sensitive areas.",
            "Prevention advice and follow-up options for continued protection."
        )
    ),
    "Building Protection" to DetailContent(
        title = "Building Protection",
        introduction = "Apartment buildings and shared properties need coordinated pest management because pests move through drains, ducts, service shafts and common areas. A planned program treats the whole risk, not only one unit.",
        problems = listOf(
            "Activity can travel between flats, floors and shared facilities.",
            "Waste rooms, parking, gardens and drains create breeding zones.",
            "Uncoordinated treatments may shift pests to another area.",
            "Recurring complaints can affect hygiene, residents and reputation."
        ),
        solutionTitle = "What Our Building Service Includes",
        solutions = listOf(
            "A survey of common areas, access routes and high-risk zones.",
            "A coordinated treatment schedule with minimal resident disruption.",
            "Monitoring and documented findings for property management.",
            "Practical recommendations for sanitation, proofing and maintenance."
        )
    ),
    "Office Pest Control" to DetailContent(
        title = "Office Pest Control",
        introduction = "Workplaces need discreet pest control that protects staff, visitors, equipment and operations. Our plan focuses on early detection and treatments scheduled around the way your business works.",
        problems = listOf(
            "Pests can damage stock, cables, furnishings and equipment.",
            "Sightings undermine staff confidence and customer trust.",
            "Pantries, waste areas and deliveries create recurring pest risks.",
            "Poor control can affect hygiene standards and business continuity."
        ),
        solutionTitle = "What Our Office Service Includes",
        solutions = listOf(
            "A discreet site inspection and risk-based service plan.",
            "Treatments scheduled to reduce interruption to working hours.",
            "Monitoring of pantries, storage, waste and access points.",
            "Clear recommendations and follow-up support for your team."
        )
    ),
    "Industrial Pest Control" to DetailContent(
        title = "Industrial Pest Control",
        introduction = "Factories, warehouses and production sites need a structured pest-management program that protects people, materials, machinery and compliance requirements across large and complex facilities.",
        problems = listOf(
            "Pests can contaminate raw materials, finished goods and production zones.",
            "Rodents and insects may damage stock, wiring, packaging and equipment.",
            "Loading bays, drains and storage areas create multiple entry routes.",
            "Uncontrolled activity can interrupt operations and affect audit results."
        ),
        solutionTitle = "What Our Industrial Service Includes",
        solutions = listOf(
            "A detailed site survey covering production, storage and external risks.",
            "A documented control plan suited to workflows and sensitive zones.",
            "Discreet monitoring with scheduled inspection and treatment visits.",
            "Trend reporting and practical corrective actions for your facility team."
        )
    )
)

@Composable
fun ServiceDetailScreen(
    detailKey: String,
    savedName: String,
    savedMobile: String,
    savedEmail: String,
    onBack: () -> Unit,
    onInquiry: (name: String, mobile: String, email: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val content = detailContent[detailKey] ?: detailContent.getValue("Home Pest Control")
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var showInquiryDialog by rememberSaveable { mutableStateOf(false) }
    var showSubmittedMessage by rememberSaveable { mutableStateOf(false) }
    val detailBanners = homeHeroSlides.map { banner ->
        banner.copy(onClick = { showInquiryDialog = true })
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DetailCream)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                top = statusBarPadding + 14.dp,
                end = 18.dp,
                bottom = navigationBarPadding + 104.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                DetailHeader(
                    title = content.title,
                    onBack = onBack
                )
            }
            item {
                InfiniteBannerCarousel(
                    banners = detailBanners,
                    placeholderRes = R.drawable.home_hero_pest_control,
                    autoScrollIntervalMillis = 4_000L
                )
            }
            item {
                DetailInformationCard(
                    title = "About ${content.title}",
                    body = content.introduction
                )
            }
            item {
                DetailBulletCard(
                    title = "Problems They Cause",
                    bullets = content.problems
                )
            }
            item {
                DetailBulletCard(
                    title = content.solutionTitle,
                    bullets = content.solutions
                )
            }
            if (showSubmittedMessage) {
                item {
                    Text(
                        text = "Thank you! Your inquiry details have been saved.",
                        color = Color(0xFF2D6A4F),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEAF5EE), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    )
                }
            }
        }

        Button(
            onClick = { showInquiryDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    bottom = navigationBarPadding + 14.dp
                )
                .height(64.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF934619), Color(0xFFB35B27), Color(0xFF7B3517))
                    ),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Text(
                text = "Book an Inquiry",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.size(14.dp))
            Text(text = "→", fontSize = 24.sp)
        }
    }

    if (showInquiryDialog) {
        InquiryDialog(
            initialName = savedName,
            initialMobile = savedMobile,
            initialEmail = savedEmail,
            serviceName = content.title,
            onDismiss = { showInquiryDialog = false },
            onSubmit = { name, mobile, email ->
                onInquiry(name, mobile, email)
                showInquiryDialog = false
                showSubmittedMessage = true
            }
        )
    }
}

@Composable
private fun DetailHeader(title: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF3E9E1))
                .clickable(
                    onClickLabel = "Back",
                    onClick = onBack
                )
        ) {
            Text(
                text = "‹",
                color = DetailEspresso,
                fontSize = 38.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(Modifier.size(15.dp))
        Text(
            text = title,
            color = DetailEspresso,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun DetailInformationCard(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = DetailCocoa.copy(alpha = 0.08f)
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Text(
            text = title,
            color = DetailEspresso,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(11.dp))
        Text(
            text = body,
            color = DetailMuted,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun DetailBulletCard(title: String, bullets: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(13.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = DetailCocoa.copy(alpha = 0.08f)
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Text(
            text = title,
            color = DetailEspresso,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        bullets.forEach { bullet ->
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .size(22.dp)
                        .background(DetailCocoa, CircleShape)
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.size(11.dp))
                Text(
                    text = bullet,
                    color = DetailMuted,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InquiryDialog(
    initialName: String,
    initialMobile: String,
    initialEmail: String,
    serviceName: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }
    var mobile by rememberSaveable(initialMobile) { mutableStateOf(initialMobile) }
    var email by rememberSaveable(initialEmail) { mutableStateOf(initialEmail) }
    var showErrors by rememberSaveable { mutableStateOf(false) }
    val nameValid = name.trim().length >= 2
    val mobileValid = isValidInquiryMobile(mobile)
    val emailValid = email.isNotBlank() &&
        Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = DetailCream,
        title = {
            Column {
                Text(
                    text = "Book an Inquiry",
                    color = DetailEspresso,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = serviceName,
                    color = DetailCocoa,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.imePadding()
            ) {
                InquiryTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full name",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                    error = "Enter your full name".takeIf { showErrors && !nameValid }
                )
                InquiryTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = "Mobile number",
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                    error = "Enter a valid mobile number".takeIf { showErrors && !mobileValid }
                )
                InquiryTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                    error = "Enter a valid email address".takeIf { showErrors && !emailValid }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = DetailMuted, fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    showErrors = true
                    if (nameValid && mobileValid && emailValid) {
                        onSubmit(name.trim(), mobile.trim(), email.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DetailCocoa),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("INQUIRE NOW", fontWeight = FontWeight.ExtraBold)
            }
        }
    )
}

@Composable
private fun InquiryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = error?.let { message -> { Text(message) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization
        ),
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DetailEspresso,
            unfocusedTextColor = DetailEspresso,
            focusedBorderColor = DetailCocoa,
            unfocusedBorderColor = DetailBorder,
            focusedLabelColor = DetailCocoa,
            unfocusedLabelColor = DetailMuted,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun isValidInquiryMobile(mobile: String): Boolean {
    val allowedCharacters = mobile.all {
        it.isDigit() || it == '+' || it == ' ' || it == '-' || it == '(' || it == ')'
    }
    return allowedCharacters && mobile.count(Char::isDigit) in 10..15
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ServiceDetailScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        ServiceDetailScreen(
            detailKey = "Ants",
            savedName = "Ashish Merugu",
            savedMobile = "9876543210",
            savedEmail = "ashish@example.com",
            onBack = {},
            onInquiry = { _, _, _ -> }
        )
    }
}
