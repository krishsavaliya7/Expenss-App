package com.splitsmart.app.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SignupScreen(uiState: AuthUiState, onSignup: (String, String, String, String, String, String) -> Unit, onNavigateToLogin: () -> Unit, onClearError: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    var fullName by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }; var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }; var upiId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }; var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }; var localError by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), MaterialTheme.colorScheme.surface)))) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(32.dp))
            Text("Create Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Step ${step + 1} of 2", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = { if (step == 0) 0.5f else 1f }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                AnimatedContent(step, label = "step") { s ->
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (s == 0) {
                            Text("Personal Info", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                            OutlinedTextField(fullName, { fullName = it; localError = null; onClearError() }, label = { Text("Full Name") }, leadingIcon = { Icon(Icons.Filled.Person, null) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }))
                            OutlinedTextField(email, { email = it; localError = null; onClearError() }, label = { Text("Email") }, leadingIcon = { Icon(Icons.Filled.Email, null) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next))
                            OutlinedTextField(username, { username = it; localError = null; onClearError() }, label = { Text("Username") }, leadingIcon = { Icon(Icons.Filled.Person, null) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
                            OutlinedTextField(phoneNumber, { phoneNumber = it; localError = null; onClearError() }, label = { Text("Phone Number") }, leadingIcon = { Icon(Icons.Filled.Phone, null) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }))
                            Button(onClick = { if (fullName.isBlank() || email.isBlank() || username.isBlank() || phoneNumber.isBlank()) localError = "All fields are required" else { step = 1; localError = null } }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) { Text("Next", style = MaterialTheme.typography.titleMedium) }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = { step = 0 }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }; Text("Security & Payment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
                            OutlinedTextField(upiId, { upiId = it; localError = null; onClearError() }, label = { Text("UPI ID") }, leadingIcon = { Icon(Icons.Filled.AccountBalance, null) }, placeholder = { Text("name@bank") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp))
                            OutlinedTextField(password, { password = it; localError = null; onClearError() }, label = { Text("Password") }, leadingIcon = { Icon(Icons.Filled.Lock, null) }, trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, "Toggle") } }, singleLine = true, visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next))
                            OutlinedTextField(confirmPassword, { confirmPassword = it; localError = null; onClearError() }, label = { Text("Confirm Password") }, leadingIcon = { Icon(Icons.Filled.Lock, null) }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done))
                            Button(onClick = { when { upiId.isBlank() || password.isBlank() -> localError = "All fields are required"; password.length < 6 -> localError = "Password must be at least 6 characters"; password != confirmPassword -> localError = "Passwords don't match"; else -> onSignup(email, username, fullName, phoneNumber, upiId, password) } }, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), enabled = !uiState.isLoading) {
                                if (uiState.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp) else Text("Create Account", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        AnimatedVisibility(localError != null || uiState.error != null) { Text(localError ?: uiState.error ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) { Text("Already have an account? Sign In") }
        }
    }
}
