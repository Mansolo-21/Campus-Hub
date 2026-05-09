package com.nohari.campus_hub.screens.Admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CreateTeacherScreen(
    navController: NavController,
    onCreateTeacher: (String, String, String) -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                "Create Teacher",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it.trim() },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it.trim() },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    if (showConfirm) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(
                            if (showConfirm) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {

                    val cleanName = fullName.trim()
                    val cleanEmail = email.trim().lowercase()
                    val cleanPassword = password.trim()
                    val cleanConfirm = confirmPassword.trim()

                    when {

                        cleanName.isBlank() ||
                                cleanEmail.isBlank() ||
                                cleanPassword.isBlank() ||
                                cleanConfirm.isBlank() -> {
                            errorMessage = "All fields are required"
                            return@Button
                        }

                        cleanPassword.length < 6 -> {
                            errorMessage = "Password too short"
                            return@Button
                        }

                        cleanPassword != cleanConfirm -> {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }

                        else -> {

                            isLoading = true
                            errorMessage = ""

                            val teacherId = db.collection("teachers").document().id

                            val teacher = hashMapOf(
                                "id" to teacherId,
                                "fullName" to cleanName,
                                "email" to cleanEmail,
                                "role" to "teacher",
                                "status" to "active"
                            )

                            db.collection("teachers")
                                .document(teacherId)
                                .set(teacher)
                                .addOnSuccessListener {

                                    isLoading = false

                                    onCreateTeacher(
                                        cleanName,
                                        cleanEmail,
                                        cleanPassword
                                    )
                                }
                                .addOnFailureListener {

                                    isLoading = false
                                    errorMessage = it.message ?: "Failed to create teacher"
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create Teacher")
                }
            }

            if (errorMessage.isNotEmpty()) {

                Spacer(Modifier.height(10.dp))

                Text(
                    text = errorMessage,
                    color = Color.Red
                )
            }
        }
    }
}