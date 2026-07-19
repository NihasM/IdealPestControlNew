package com.idealpestcontrol.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idealpestcontrol.R
import com.idealpestcontrol.ui.theme.IdealPestControlTheme

private data class AuthColors(
    val background: Color,
    val surface: Color,
    val accent: Color,
    val onAccent: Color,
    val title: Color,
    val body: Color,
    val border: Color
)

private val LightAuthColors = AuthColors(
    background = Color(0xFFF9F8F7),
    surface = Color(0xFFEDEAE8),
    accent = Color(0xFF965C4C),
    onAccent = Color.White,
    title = Color(0xFF2F2D2C),
    body = Color(0xFF66615F),
    border = Color(0xFFD5CFCC)
)

private val DarkAuthColors = AuthColors(
    background = Color(0xFF121212),
    surface = Color(0xFF242222),
    accent = Color(0xFFD8927E),
    onAccent = Color(0xFF23110C),
    title = Color(0xFFF3F1F0),
    body = Color(0xFFCAC6C4),
    border = Color(0xFF4D4846)
)

@Composable
fun LoginSignUpScreen(
    modifier: Modifier = Modifier,
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onSignUp: (
        name: String,
        email: String,
        mobile: String,
        password: String
    ) -> Unit = { _, _, _, _ -> },
    onForgotPassword: () -> Unit = {}
) {
    val darkTheme = isSystemInDarkTheme()
    val colors = if (darkTheme) DarkAuthColors else LightAuthColors
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var isLogin by rememberSaveable { mutableStateOf(true) }
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameError = if (fullName.trim().length < 2) {
        stringResource(R.string.name_validation_error)
    } else {
        null
    }
    val emailError = if (!isValidEmail(email)) {
        stringResource(R.string.email_validation_error)
    } else {
        null
    }
    val mobileError = if (!isValidMobile(mobile)) {
        stringResource(R.string.mobile_validation_error)
    } else {
        null
    }
    val passwordError = if (!isValidPassword(password)) {
        stringResource(R.string.password_validation_error)
    } else {
        null
    }
    val confirmPasswordError = if (confirmPassword != password || confirmPassword.isEmpty()) {
        stringResource(R.string.confirm_password_validation_error)
    } else {
        null
    }
    val formIsValid = emailError == null && passwordError == null && (
        isLogin || (
            nameError == null &&
                mobileError == null &&
                confirmPasswordError == null
            )
        )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(top = statusBarHeight, bottom = navigationBarHeight)
    ) {
        WorkerIllustration(
            darkTheme = darkTheme,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(if (isLogin) 215.dp else 165.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 220.dp)
        ) {
            Text(
                text = stringResource(R.string.auth_welcome),
                color = colors.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.auth_choose_option),
                color = colors.body,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(26.dp))

            AuthModeSelector(
                isLogin = isLogin,
                colors = colors,
                onModeSelected = {
                    isLogin = it
                    showErrors = false
                }
            )
            Spacer(Modifier.height(26.dp))

            if (!isLogin) {
                AuthTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = stringResource(R.string.full_name),
                    colors = colors,
                    errorText = nameError.takeIf { showErrors }
                )
                Spacer(Modifier.height(12.dp))
            }

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                colors = colors,
                keyboardType = KeyboardType.Email,
                errorText = emailError.takeIf { showErrors }
            )

            if (!isLogin) {
                Spacer(Modifier.height(12.dp))
                AuthTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = stringResource(R.string.mobile_number),
                    colors = colors,
                    keyboardType = KeyboardType.Phone,
                    errorText = mobileError.takeIf { showErrors }
                )
            }

            Spacer(Modifier.height(12.dp))
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                colors = colors,
                keyboardType = KeyboardType.Password,
                isPassword = true,
                errorText = passwordError.takeIf { showErrors }
            )

            if (isLogin) {
                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = colors.accent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Spacer(Modifier.height(12.dp))
                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = stringResource(R.string.confirm_password),
                    colors = colors,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    errorText = confirmPasswordError.takeIf { showErrors }
                )
                Spacer(Modifier.height(20.dp))
            }

            Button(
                onClick = {
                    showErrors = true
                    if (formIsValid) {
                        if (isLogin) {
                            onLogin(email.trim(), password)
                        } else {
                            onSignUp(
                                fullName.trim(),
                                email.trim(),
                                mobile.trim(),
                                password
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.onAccent
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(
                        if (isLogin) R.string.login else R.string.create_account
                    ),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AuthModeSelector(
    isLogin: Boolean,
    colors: AuthColors,
    onModeSelected: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.surface)
            .padding(4.dp)
    ) {
        AuthModeOption(
            text = stringResource(R.string.login),
            selected = isLogin,
            colors = colors,
            onClick = { onModeSelected(true) },
            modifier = Modifier.weight(1f)
        )
        AuthModeOption(
            text = stringResource(R.string.sign_up),
            selected = !isLogin,
            colors = colors,
            onClick = { onModeSelected(false) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthModeOption(
    text: String,
    selected: Boolean,
    colors: AuthColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) colors.accent else Color.Transparent)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = if (selected) colors.onAccent else colors.body,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    colors: AuthColors,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.title,
            unfocusedTextColor = colors.title,
            cursorColor = colors.accent,
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.border,
            focusedLabelColor = colors.accent,
            unfocusedLabelColor = colors.body,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun isValidEmail(email: String): Boolean =
    email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

private fun isValidMobile(mobile: String): Boolean {
    val allowedCharacters = mobile.all {
        it.isDigit() || it == '+' || it == ' ' || it == '-' || it == '(' || it == ')'
    }
    val digitCount = mobile.count(Char::isDigit)
    return allowedCharacters && digitCount in 10..15
}

private fun isValidPassword(password: String): Boolean =
    password.length >= 8 &&
        password.any(Char::isDigit) &&
        password.any { !it.isLetterOrDigit() }

@Composable
private fun WorkerIllustration(
    darkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val lineColor = if (darkTheme) 255f else 0f
    val lineArtFilter = ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                0f, 0f, 0f, 0f, lineColor,
                0f, 0f, 0f, 0f, lineColor,
                0f, 0f, 0f, 0f, lineColor,
                -1f, 0f, 0f, 0f, 255f
            )
        )
    )

    Image(
        painter = painterResource(R.drawable.ic_pest_control_worker),
        contentDescription = stringResource(R.string.worker_content_description),
        contentScale = ContentScale.Fit,
        colorFilter = lineArtFilter,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginSignUpScreenPreview() {
    IdealPestControlTheme(dynamicColor = false) {
        LoginSignUpScreen()
    }
}
