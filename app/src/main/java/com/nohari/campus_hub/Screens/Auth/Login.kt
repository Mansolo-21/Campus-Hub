package com.nohari.campus_hub.Screens.Auth

import com.nohari.campus_hub.R
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            .background(Color(0xFFF5F7FB))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Welcome Back 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Login to continue",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // EMAIL CARD FIELD
        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // PASSWORD CARD FIELD
        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                singleLine = true,
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Hide" else "Show")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔵 BLUE LOGIN BUTTON
        Button(
            onClick = {
                val cleanEmail = email.trim()

                when {
                    cleanEmail.isEmpty() ||
                            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                        errorMsg = "Enter valid email"
                    }

                    password.length < 6 -> {
                        errorMsg = "Password too short"
                    }

                    else -> {
                        isLoading = true

                        scope.launch {
                            val result = authRepo.login(cleanEmail, password)

                            isLoading = false

                            result.onSuccess {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6) // 🔵 BLUE
            ),
            enabled = !isLoading
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            navController.navigate(Routes.REGISTER)
        }) {
            Text("Don't have an account? Register")
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }
    }
}