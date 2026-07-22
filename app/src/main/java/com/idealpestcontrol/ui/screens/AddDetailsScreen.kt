package com.idealpestcontrol.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

private val DetailsBackground = Color(0xFFFFF9F2)
private val DetailsAccent = Color(0xFFA7542A)
private val DetailsText = Color(0xFF3A241C)
private val DetailsBody = Color(0xFF79685E)
private val DetailsBorder = Color(0xFFD8C4B4)

@Composable
fun AddDetailsScreen(
    modifier: Modifier = Modifier,
    onContinue: (name: String, mobile: String, email: String) -> Unit = { _, _, _ -> }
) {
    var name by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameError = stringResource(R.string.name_validation_error)
        .takeIf { name.trim().length < 2 }
    val mobileError = stringResource(R.string.mobile_validation_error)
        .takeIf { !isValidDetailsMobile(mobile) }
    val emailError = stringResource(R.string.email_validation_error)
        .takeIf { !isValidDetailsEmail(email) }
    val formIsValid = nameError == null && mobileError == null && emailError == null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DetailsBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.profile_details_illustration),
            contentDescription = stringResource(R.string.details_illustration_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.58f)
                .offset(x = 18.dp)
                .navigationBarsPadding()
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(24.dp))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 34.dp, bottom = 190.dp)
        ) {
            Text(
                text = stringResource(R.string.details_title),
                color = DetailsText,
                fontSize = 31.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 37.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.details_subtitle),
                color = DetailsBody,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(30.dp))

            DetailsTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(R.string.full_name),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                errorText = nameError.takeIf { showErrors }
            )
            Spacer(Modifier.height(14.dp))
            DetailsTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = stringResource(R.string.mobile_number),
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                errorText = mobileError.takeIf { showErrors }
            )
            Spacer(Modifier.height(14.dp))
            DetailsTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                errorText = emailError.takeIf { showErrors }
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    showErrors = true
                    if (formIsValid) {
                        onContinue(name.trim(), mobile.trim(), email.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DetailsAccent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.continue_to_home),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

@Composable
private fun DetailsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    errorText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = errorText != null,
        supportingText = errorText?.let { message ->
            { Text(message) }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        shape = RoundedCornerShape(17.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DetailsText,
            unfocusedTextColor = DetailsText,
            cursorColor = DetailsAccent,
            focusedBorderColor = DetailsAccent,
            unfocusedBorderColor = DetailsBorder,
            errorBorderColor = Color(0xFFB3261E),
            focusedLabelColor = DetailsAccent,
            unfocusedLabelColor = DetailsBody,
            errorLabelColor = Color(0xFFB3261E),
            errorSupportingTextColor = Color(0xFFB3261E),
            focusedContainerColor = Color.White.copy(alpha = 0.94f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.84f)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun isValidDetailsEmail(email: String): Boolean =
    email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

private fun isValidDetailsMobile(mobile: String): Boolean {
    val allowedCharacters = mobile.all {
        it.isDigit() || it == '+' || it == ' ' || it == '-' || it == '(' || it == ')'
    }
    return allowedCharacters && mobile.count(Char::isDigit) in 10..15
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddDetailsScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        AddDetailsScreen()
    }
}
