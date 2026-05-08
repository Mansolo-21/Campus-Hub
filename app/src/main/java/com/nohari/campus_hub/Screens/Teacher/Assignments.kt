package com.nohari.campus_hub.Screens.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var assignments by remember {
        mutableStateOf(listOf<Assignment>())
    }

    LaunchedEffect(Unit) {

        db.collection("announcements")
            .whereEqualTo("type", "assignment")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {

                    val list = snapshot.documents.mapNotNull { doc ->

                        Assignment(
                            title = doc.getString("title") ?: "",
                            subject = doc.getString("subject") ?: "",
                            dueDate = doc.getString("dueDate") ?: ""
                        )
                    }

                    assignments = list
                }
            }
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = { Text("Assignments") },
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FB))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(assignments) { assignment ->
                AssignmentCard(assignment)
            }
        }
    }
}

/* ========================================================= */
/* ===================== COMPONENTS ======================== */
/* ========================================================= */
@Composable
fun AssignmentCard(
    assignment: Assignment
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = assignment.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = assignment.subject,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFEEF2FF)
            ) {

                Text(
                    text = "Due: ${assignment.dueDate}",
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 6.dp
                    ),
                    color = Color(0xFF4F46E5),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
data class Assignment(
    val title: String,
    val subject: String,
    val dueDate: String
)