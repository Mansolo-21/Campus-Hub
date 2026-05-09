package com.nohari.campus_hub.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User


@Composable
fun UserListScreen(navController: NavController) {

    val users = remember { mutableStateListOf<User>() }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {

        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { snapshot ->

                val result = snapshot.toObjects(User::class.java)

                println("🔥 USERS FOUND: ${result.size}")

                users.clear()
                users.addAll(result)
            }
            .addOnFailureListener { e ->

                println("❌ FIRESTORE ERROR: ${e.message}")
            }
    }

    if (users.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("No users found")
        }

    } else {

        LazyColumn {

            items(users, key = { it.uid }) { user ->

                Text(
                    text = user.fullName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("chat/${user.uid}")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}