package com.nohari.campus_hub.screens.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nohari.campus_hub.R
import com.nohari.campus_hub.data.repository.AuthRepository
import com.nohari.campus_hub.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {

    val repo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally) // centers it
                .clip(CircleShape) // makes it circular
        )
        Spacer(modifier = Modifier.height(30.dp))

        // FULL NAME
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // CONFIRM PASSWORD
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Text(if (confirmPasswordVisible) "Hide" else "Show")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val cleanEmail = email.trim()

                // VALIDATION
                when {
                    fullName.isBlank() -> errorMsg = "Full name required"
                    cleanEmail.isEmpty() ||
                            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                        errorMsg = "Enter a valid email"

                    password.length < 6 ->
                        errorMsg = "Password must be at least 6 characters"

                    password != confirmPassword ->
                        errorMsg = "Passwords do not match"

                    else -> {
                        scope.launch {
                            val result = repo.register(fullName, cleanEmail, password)
                            result.onSuccess {
                                errorMsg = ""
                                navController.navigate("login")
                            }.onFailure {
                                errorMsg = it.message ?: "Registration failed"
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        TextButton(onClick = {navController.navigate(Routes.LOGIN)}) {
            Text("Already have an account? Login")
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }
    }
}