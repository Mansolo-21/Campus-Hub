package com.nohari.campus_hub.Screens.Marketplace

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
    itemId: String
) {

    val db = FirebaseFirestore.getInstance()
    var item by remember { mutableStateOf<Item?>(null) }

    // 🔥 Fetch single item
    LaunchedEffect(itemId) {
        db.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { doc ->
                item = doc.toObject(Item::class.java)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Details") }
            )
        }
    ) { padding ->

        item?.let {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AsyncImage(
                    model = it.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "KES ${it.price}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = it.description
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }

        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}