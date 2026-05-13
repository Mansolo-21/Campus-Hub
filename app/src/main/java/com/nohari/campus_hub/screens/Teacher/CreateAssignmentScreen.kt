package com.nohari.campus_hub.screens.Teacher

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssignmentScreen(
    navController: NavController
) {

    val db = FirebaseFirestore.getInstance()

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Create Assignment")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Assignment Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Due Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val assignment = hashMapOf(

                        "title" to title,

                        "message" to """
                Subject: $subject
                
                Due Date: $dueDate
            """.trimIndent(),

                        "timestamp" to System.currentTimeMillis().toString(),

                        "type" to "assignment"
                    )

                    db.collection("announcements")
                        .add(assignment)
                        .addOnSuccessListener {

                            Toast.makeText(
                                navController.context,
                                "Assignment Added",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.popBackStack()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                navController.context,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F46E5)
                )
            ) {

                Text("Post Assignment")
            }
        }
    }
}