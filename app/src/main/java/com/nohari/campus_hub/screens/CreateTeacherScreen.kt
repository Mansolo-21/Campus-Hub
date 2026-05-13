package com.nohari.campus_hub.screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF334155)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Create Teacher Account",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add a new teacher to the system",
                color = Color.LightGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it.trim() },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it.trim() },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),

                        visualTransformation =
                            if (showPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    showPassword = !showPassword
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (showPassword)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,

                                    contentDescription = null
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it.trim() },
                        label = { Text("Confirm Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),

                        visualTransformation =
                            if (showConfirm) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    showConfirm = !showConfirm
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (showConfirm)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,

                                    contentDescription = null
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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

                                    val teacherId =
                                        db.collection("teachers")
                                            .document()
                                            .id

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

                                            errorMessage =
                                                it.message
                                                    ?: "Failed to create teacher"
                                        }
                                }
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        shape = RoundedCornerShape(18.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Create Teacher",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (errorMessage.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}