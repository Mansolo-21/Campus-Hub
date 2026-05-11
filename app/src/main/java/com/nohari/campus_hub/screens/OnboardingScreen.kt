package com.nohari.campus_hub.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.nohari.campus_hub.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.data.SessionManager
import com.nohari.campus_hub.navigation.Routes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


/* ========================================================= */
/* ===================== ONBOARDING ======================== */
/* ========================================================= */

@Composable
fun OnboardingScreen(navController: NavController) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4F46E5),
                        Color(0xFF111827)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement = Arrangement.Center,

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // APP LOGO
            Card(
                modifier = Modifier.size(130.dp),
                shape = CircleShape
            ) {

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // APP NAME
            Text(
                text = "Campus Hub",
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // DESCRIPTION
            Text(
                text = "Smart Digital Campus Platform",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text =
                    "Manage announcements, assignments, chats, marketplace activities, and campus events all in one modern platform designed for students, teachers and administrators.",

                color = Color.White.copy(alpha = 0.75f),

                fontSize = 15.sp,

                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            // CREATE CAMPUS BUTTON
            Button(
                onClick = {

                    // SAVE ONBOARDING STATE
                    SessionManager.setOnboardingDone(context, true)

                    navController.navigate(Routes.REGISTER_CAMPUS)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = "Create New Campus",
                    color = Color(0xFF4F46E5),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // EXISTING CAMPUS BUTTON
            OutlinedButton(
                onClick = {

                    // SAVE ONBOARDING STATE
                    SessionManager.setOnboardingDone(context, true)

                    navController.navigate(Routes.CAMPUS_LIST)
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {

                Text(
                    text = "My Campus Already Exists",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}

/* ========================================================= */
/* ====================== CAMPUS LIST ====================== */
/* ========================================================= */

data class CampusModel(
    val id: String = "",
    val campusName: String = "",
    val logoUrl: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusListScreen(
    navController: NavController
) {

    val db = FirebaseFirestore.getInstance()

    var campusList by remember {
        mutableStateOf(listOf<CampusModel>())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {

        db.collection("campuses")
            .get()
            .addOnSuccessListener { result ->

                val list = mutableListOf<CampusModel>()

                for (doc in result.documents) {

                    val campus = CampusModel(
                        id = doc.getString("id") ?: "",
                        campusName = doc.getString("campusName") ?: "",
                        logoUrl = doc.getString("logoUrl") ?: ""
                    )

                    list.add(campus)
                }

                campusList = list
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Registered Campuses")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FB))
        ) {

            if (loading) {

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(campusList) { campus ->

                        CampusCard(
                            campus = campus
                        ) {

                            navController.navigate(Routes.LOGIN)
                        }
                    }
                }
            }
        }
    }
}

/* ========================================================= */
/* ====================== CAMPUS CARD ====================== */
/* ========================================================= */

@Composable
fun CampusCard(
    campus: CampusModel,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.size(75.dp),
                shape = CircleShape
            ) {

                if (campus.logoUrl.isNotEmpty()) {

                    AsyncImage(
                        model = campus.logoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = campus.campusName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tap to continue",
                    color = Color.Gray
                )
            }
        }
    }
}