package com.nohari.campus_hub.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun CreateTeacherScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        Text("Create Teacher Account")

        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(password, { password = it }, label = { Text("Password") })

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                    val user = hashMapOf(
                        "uid" to uid,
                        "email" to email,
                        "role" to "teacher"
                    )

                    db.collection("users").document(uid).set(user)

                    message = "Teacher created"

                    // 🔥 Logout teacher → return admin
                    auth.signOut()
                    navController.popBackStack()
                }

        }) {
            Text("Create Teacher")
        }

        Text(message)
    }
}