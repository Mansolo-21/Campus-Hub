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
fun LoginScreen(navController: NavController) {

    val authRepo = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally) // centers it
                .clip(CircleShape) // makes it circular
        )

        Spacer(modifier = Modifier.height(30.dp))



        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LOGIN BUTTON
        Button(
            onClick = {
                val cleanEmail = email.trim()

                when {
                    cleanEmail.isEmpty() ||
                            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                        errorMsg = "Enter a valid email"
                    }

                    password.length < 6 -> {
                        errorMsg = "Password must be at least 6 characters"
                    }

                    else -> {
                        isLoading = true

                        scope.launch {
                            val result = authRepo.login(cleanEmail, password)

                            isLoading = false

                            result.onSuccess {
                                errorMsg = ""

                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }

                            }.onFailure {
                                errorMsg = it.message ?: "Login failed"
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // NAV TO REGISTER}
             TextButton(onClick = {navController.navigate(Routes.REGISTER)}) {
                Text("Don't Have an account? Register")
                }
        Spacer(modifier = Modifier.height(8.dp))

        // ERROR MESSAGE
        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }
    }
}