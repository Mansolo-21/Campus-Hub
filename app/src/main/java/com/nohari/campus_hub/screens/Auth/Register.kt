package com.nohari.campus_hub.screens.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User
import com.nohari.campus_hub.navigation.Routes

@Composable
fun RegisterScreen(
    navController: NavController
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var fullName by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var confirmPassword by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4F46E5),
                        Color(0xFF111827)
                    )
                )
            )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),

            shape = RoundedCornerShape(30.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(25.dp)
                            .clip(CircleShape),
                        tint = Color(0xFF4F46E5)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Create Account",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Join Campus Hub",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it.trim()
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it.trim()
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),

                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible)
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
                    onValueChange = {
                        confirmPassword = it.trim()
                    },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),

                    visualTransformation =
                        if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                confirmPasswordVisible =
                                    !confirmPasswordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (confirmPasswordVisible)
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

                        when {

                            fullName.isBlank() -> {

                                errorMsg = "Enter full name"
                            }

                            email.isBlank() ||
                                    !Patterns.EMAIL_ADDRESS
                                        .matcher(email)
                                        .matches() -> {

                                errorMsg = "Enter valid email"
                            }

                            password.length < 6 -> {

                                errorMsg =
                                    "Password must be at least 6 characters"
                            }

                            password != confirmPassword -> {

                                errorMsg =
                                    "Passwords do not match"
                            }

                            else -> {

                                errorMsg = ""
                                loading = true

                                auth.createUserWithEmailAndPassword(
                                    email,
                                    password
                                ).addOnCompleteListener { task ->

                                    if (task.isSuccessful) {

                                        val firebaseUser =
                                            auth.currentUser

                                        if (firebaseUser != null) {

                                            val uid = firebaseUser.uid

                                            val user = User(
                                                uid = uid,
                                                fullName = fullName,
                                                email = email,
                                                role = "student"
                                            )

                                            db.collection("users")
                                                .document(uid)
                                                .set(user)
                                                .addOnSuccessListener {

                                                    loading = false

                                                    navController.navigate(
                                                        Routes.HOME
                                                    ) {

                                                        popUpTo(
                                                            Routes.REGISTER
                                                        ) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                .addOnFailureListener { e ->

                                                    loading = false

                                                    errorMsg =
                                                        e.message
                                                            ?: "Failed to save user"
                                                }

                                        } else {

                                            loading = false

                                            errorMsg =
                                                "User creation failed"
                                        }

                                    } else {

                                        loading = false

                                        errorMsg =
                                            task.exception?.message
                                                ?: "Registration failed"
                                    }
                                }
                            }
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5)
                    )
                ) {

                    if (loading) {

                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp)
                        )

                    } else {

                        Text(
                            text = "Create Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = {
                        navController.navigate(Routes.LOGIN)
                    }
                ) {

                    Text("Already have an account? Login")
                }

                if (errorMsg.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}