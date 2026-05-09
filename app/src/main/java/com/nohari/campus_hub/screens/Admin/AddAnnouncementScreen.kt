package com.nohari.campus_hub.screens.Announcements

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddAnnouncementScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isAssignment by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Create Post", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row {

            Checkbox(
                checked = isAssignment,
                onCheckedChange = { isAssignment = it }
            )

            Text("Mark as Assignment")
        }

        Spacer(Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                if (title.isEmpty() || message.isEmpty()) {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(Date())

                val data = hashMapOf(
                    "title" to title,
                    "message" to message,
                    "type" to if (isAssignment) "assignment" else "announcement",
                    "timestamp" to date
                )

                db.collection("announcements")
                    .add(data)
                    .addOnSuccessListener {

                        Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                    .addOnFailureListener {

                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
            }
        ) {
            Text("Post")
        }
    }
}