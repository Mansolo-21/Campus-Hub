package com.nohari.campus_hub.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.R
import com.nohari.campus_hub.navigation.Routes
import kotlinx.coroutines.delay
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