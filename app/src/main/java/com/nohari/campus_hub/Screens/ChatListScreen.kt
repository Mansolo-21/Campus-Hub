package com.nohari.campus_hub.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nohari.campus_hub.models.User


@Composable
fun ChatListScreen(
    navController: NavController,
    users: List<User> // pass from ViewModel or Firebase
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(users) { user ->

                ChatUserItem(
                    user = user,
                    onClick = {
                        navController.navigate("chat/${user.uid}")
                    }
                )
            }
        }
    }
}
@Composable
fun ChatUserItem(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // avatar placeholder
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primary
            ) {}

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = user.fullname,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}