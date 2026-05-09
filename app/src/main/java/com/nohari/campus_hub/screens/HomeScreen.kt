package com.nohari.campus_hub.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.navigation.Routes
import kotlinx.coroutines.tasks.await
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var fullName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("student") }

    var campusName by remember { mutableStateOf("Campus Hub") }

    var campusLogo by remember {
        mutableStateOf(
            "https://images.unsplash.com/photo-1523050854058-8df90110c9f1"
        )
    }

    LaunchedEffect(Unit) {

        val uid = auth.currentUser?.uid

        if (uid != null) {

            try {

                val userDoc = db.collection("users")
                    .document(uid)
                    .get()
                    .await()

                fullName = userDoc.getString("fullName") ?: "Student"

                role = userDoc.getString("role")
                    ?: "student"

                val campusId = userDoc.getString("campusId")

                if (campusId != null) {

                    val campusDoc = db.collection("campuses")
                        .document(campusId)
                        .get()
                        .await()

                    campusName = campusDoc.getString("campusName")
                        ?: "Campus Hub"

                    campusLogo = campusDoc.getString("logoUrl")
                        ?: campusLogo
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // DYNAMIC ACTIONS
    val quickActions = remember(role) {

        mutableListOf(

            HomeAction(
                "Marketplace",
                Icons.Default.ShoppingCart,
                Routes.ITEMLIST,
                Color(0xFF10B981)
            ),

            HomeAction(
                "Announcements",
                Icons.Default.Info,
                Routes.ANNOUNCEMENTS,
                Color(0xFF3B82F6)
            ),

            HomeAction(
                "Events",
                Icons.Default.DateRange,
                Routes.EVENTS,
                Color(0xFFEC4899)
            ),

            HomeAction(
                "Chats",
                Icons.Default.Email,
                Routes.CHAT_LIST,
                Color(0xFF8B5CF6)
            )
        ).apply {

            if (role == "teacher") {

                add(
                    HomeAction(
                        "Add Assignment",
                        Icons.Default.Create,
                        Routes.ASSIGNMENT,
                        Color(0xFFFF9800)
                    )
                )
            }

            if (role == "admin") {

                add(
                    HomeAction(
                        "Admin Panel",
                        Icons.Default.AccountBox,
                        Routes.ADMIN_DASHBOARD,
                        Color(0xFFEF4444)
                    )
                )
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FB)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 30.dp)
        ) {

            // HERO SECTION
            item {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {

                        AsyncImage(
                            model = campusLogo,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(24.dp)
                        ) {

                            Text(
                                text = campusName,
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Welcome back, $fullName ",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {

                                Text(
                                    text = role.uppercase(),
                                    color = Color.White,
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 6.dp
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // PROFILE BUTTON
                        Card(
                            modifier = Modifier
                                .padding(18.dp)
                                .align(Alignment.TopEnd),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            )
                        ) {

                            IconButton(
                                onClick = {
                                    navController.navigate(Routes.PROFILE)
                                }
                            ) {

                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // QUICK ACTIONS
            item {

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {

                        Text(
                            text = "Quick Access",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(650.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            userScrollEnabled = false
                        ) {

                            items(quickActions) { action ->

                                PremiumActionCard(
                                    action = action
                                ) {

                                    navController.navigate(action.route)
                                }
                            }
                        }
                    }
                }
            }

            // CAMPUS UPDATE CARD
            item {

                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4F46E5)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {

                        Text(
                            text = "Campus Updates",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Stay updated with announcements, assignments, events and student activities in real-time.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // LOGOUT BUTTON
            item {

                Button(
                    onClick = {

                        auth.signOut()

                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626)
                    )
                ) {

                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/* ------------------------------------------------------ */

@Composable
fun PremiumActionCard(
    action: HomeAction,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(
                        action.color.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    tint = action.color,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column {

                Text(
                    text = action.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Open ${action.title.lowercase()} section",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

/* ------------------------------------------------------ */

data class HomeAction(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color: Color
)