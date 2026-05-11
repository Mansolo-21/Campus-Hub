package com.nohari.campus_hub.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User
import com.nohari.campus_hub.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {

    val auth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Create Account")
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Join Campus Hub",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create your student account",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                },
                label = {
                    Text("Full Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text("Email")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {

                    if (
                        fullName.isBlank() ||
                        email.isBlank() ||
                        password.isBlank()
                    ) {

                        Toast.makeText(
                            navController.context,
                            "Fill all fields",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

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

                                // FIXED USER SAVE
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

                                        Toast.makeText(
                                            navController.context,
                                            "Account created",
                                            Toast.LENGTH_SHORT
                                        ).show()

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

                                        Toast.makeText(
                                            navController.context,
                                            e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                            } else {

                                loading = false

                                Toast.makeText(
                                    navController.context,
                                    "User creation failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {

                            loading = false

                            Toast.makeText(
                                navController.context,
                                task.exception?.message
                                    ?: "Registration failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )

                } else {

                    Text("Create Account")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = {
                    navController.navigate(Routes.LOGIN)
                }
            ) {

                Text("Already have an account? Login")
            }
        }
    }
}