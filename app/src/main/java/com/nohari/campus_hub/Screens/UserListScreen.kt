package com.nohari.campus_hub.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.noharishop.models.User

@Composable
fun UserListScreen(navController: NavController) {

    val users = remember { mutableStateListOf<User>() }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener {
                users.clear()
                users.addAll(it.toObjects(User::class.java))
            }
    }

    LazyColumn {

        items(users) { user ->

            Text(
                text = user.fullname,
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