package com.nohari.campus_hub.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User

@Composable
fun UserListScreen(navController: NavController) {

    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(Unit) {

        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { snapshot ->

                val result = snapshot.documents.mapNotNull { doc ->

                    val user = doc.toObject(User::class.java)

                    user?.copy(
                        uid = doc.id.ifBlank { doc.id }
                    )
                }

                users.clear()
                users.addAll(result)
            }
    }

    if (users.isEmpty()) {

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No users found")
        }

    } else {

        LazyColumn {

            items(
                items = users,
                key = { user ->
                    user.uid.takeIf { it.isNotBlank() }
                        ?: "user-${user.hashCode()}"
                }
            ) { user ->

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