package com.nohari.campus_hub.Screens.Marketplace

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navController: NavController,
    itemId: String
) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var item by remember { mutableStateOf<Item?>(null) }
    var loading by remember { mutableStateOf(true) }

    // 🔥 Load item
    LaunchedEffect(itemId) {
        db.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener {
                item = it.toObject(Item::class.java)
                loading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            item?.let { currentItem ->

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // 🖼 IMAGE
                    AsyncImage(
                        model = currentItem.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    // 📝 NAME
                    Text(
                        text = currentItem.name,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // 💰 PRICE
                    Text(
                        text = "KES ${currentItem.price}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // 📄 DESCRIPTION
                    Text(
                        text = currentItem.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 📞 CONTACT SELLER (SIMULATED)
                    Button(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:0700000000") // replace later
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Phone, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Seller")
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser?.uid == currentItem.ownerId) {

                        Button(
                            onClick = {
                                db.collection("items")
                                    .document(currentItem.id)
                                    .delete()
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Delete Item")
                        }
                    }
                }
            }
        }
    }
}