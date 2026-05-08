package com.nohari.campus_hub.Screens.Auth

import com.nohari.campus_hub.R
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    var errorMsg by remember { mutableStateOf("") }
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
            "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text("Join CampusHub today", color = Color.Gray)

        Spacer(modifier = Modifier.height(25.dp))
        @Composable
        fun field(value: String, label: String, onChange: (String) -> Unit) {
            Card(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onChange,
                    label = { Text(label) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
            }
        }

        field(fullName, "Full Name") { fullName = it }
        Spacer(modifier = Modifier.height(10.dp))

        field(email, "Email") { email = it }
        Spacer(modifier = Modifier.height(10.dp))

        field(password, "Password") { password = it }
        Spacer(modifier = Modifier.height(10.dp))

        field(confirmPassword, "Confirm Password") { confirmPassword = it }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                when {
                    fullName.isBlank() -> errorMsg = "Enter name"
                    email.isBlank() -> errorMsg = "Enter email"
                    password.length < 6 -> errorMsg = "Weak password"
                    password != confirmPassword -> errorMsg = "Passwords don't match"

                    else -> {
                        isLoading = true

                        scope.launch {
                            val result = repo.register(fullName, email.trim(), password)

                            isLoading = false

                            result.onSuccess {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.REGISTER) { inclusive = true }
                                }
                            }.onFailure {
                                errorMsg = it.message ?: "Registration failed"
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
                containerColor = Color(0xFF4F46E5)
            ),
            enabled = !isLoading
        ) {
            Text(
                if (isLoading) "Creating..." else "Register",
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = {
            navController.navigate(Routes.LOGIN)
        }) {
            Text("Already have an account? Login")
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(errorMsg, color = MaterialTheme.colorScheme.error)
        }
    }
}