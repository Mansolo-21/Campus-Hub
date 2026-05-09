package com.nohari.campus_hub.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var logoUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {

        db.collection("campuses")
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.isEmpty) {

                    // ❌ No campus → go register
                    navController.navigate(Routes.REGISTER_CAMPUS) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }

                } else {

                    val campus = snapshot.documents.first()
                    logoUrl = campus.getString("logoUrl")

                    // show splash for a moment
                    kotlinx.coroutines.GlobalScope.launch {
                        kotlinx.coroutines.delay(1500)

                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (logoUrl != null) {

            AsyncImage(
                model = logoUrl,
                contentDescription = "Campus Logo",
                modifier = Modifier.size(140.dp)
            )

        } else {

            CircularProgressIndicator()
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun SplashPreview() {
    SplashScreen(rememberNavController())
}