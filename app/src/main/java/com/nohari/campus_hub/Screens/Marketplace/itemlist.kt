package com.nohari.campus_hub.Screens.Marketplace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.nohari.campus_hub.Data.ItemViewModel
import com.nohari.campus_hub.models.Item
import com.nohari.campus_hub.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel = remember { ItemViewModel(navController, context) }

    val items = remember { mutableStateListOf<Item>() }

    LaunchedEffect(Unit) {
        viewModel.getItems(items)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Marketplace") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.ADDITEM)
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(items) { item ->

                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable {
                            // 🔥 OPEN ITEM DETAIL
                            navController.navigate("${Routes.ITEM_DETAIL}/${item.id}")
                        }
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(item.name)
                        Text("KES ${item.price}")
                        Text(item.description)

                        Spacer(modifier = Modifier.height(10.dp))

                        // 🔥 ACTION ROW (PROPER UX)
                        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            if (currentUser?.uid == item.ownerId) {
                                Text(
                                    text = "Delete",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.clickable {
                                        viewModel.deleteItem(item)
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}