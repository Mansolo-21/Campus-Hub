package com.nohari.campus_hub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.User
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import com.nohari.campus_hub.utils.RoleManager
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController) {

    val users = remember {
        mutableStateListOf<User>()
    }
    var role by remember {
        mutableStateOf<String?>(null)
    }

    var selectedUser by remember {
        mutableStateOf<User?>(null)
    }

    var editedName by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        RoleManager.getUserRole {
            role = it
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .get()
            .addOnSuccessListener { snapshot ->

                val result = snapshot.documents.mapNotNull { doc ->

                    val user = doc.toObject(User::class.java)

                    user?.copy(
                        uid = doc.id.ifBlank { doc.id }
                    )
                }

                users.clear()
                users.addAll(result)
            }
    }

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text("Campus Chats")
                }
            )
        }

    ) { padding ->

        if (users.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No users found",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF6F7FB))
            ) {

                // HEADER CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Messages 💬",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Chat with students, teachers and admins",
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                // USER LIST
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = users,

                        key = { user ->
                            user.uid.takeIf { it.isNotBlank() }
                                ?: "user-${user.hashCode()}"
                        }

                    ) { user ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    navController.navigate(
                                        "chat/${user.uid}"
                                    )
                                },

                            shape = RoundedCornerShape(22.dp),

                            elevation = CardDefaults.cardElevation(5.dp),

                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // PROFILE CIRCLE
                                Surface(
                                    modifier = Modifier.size(56.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {

                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                // USER INFO
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = user.fullName,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = user.role.ifBlank {
                                            "Campus User"
                                        },

                                        style = MaterialTheme.typography.bodySmall,

                                        color = Color.Gray
                                    )
                                }

                                // CHAT ICON
                                // CHAT / ADMIN ACTIONS AREA
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    if (role == "admin") {

                                        Row {

                                            IconButton(
                                                onClick = {
                                                    editedName = user.fullName
                                                    selectedUser = user
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Edit"
                                                )
                                            }

                                            IconButton(
                                                onClick = {

                                                    FirebaseFirestore.getInstance()
                                                        .collection("users")
                                                        .document(user.uid)
                                                        .delete()
                                                        .addOnSuccessListener {

                                                            users.remove(user)

                                                            Toast.makeText(
                                                                context,
                                                                "User deleted",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    } else {

                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        ) {

                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                                contentDescription =null,
                                                modifier = Modifier.padding(10.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        if (selectedUser != null) {

            AlertDialog(

                onDismissRequest = {
                    selectedUser = null
                },

                title = {
                    Text("Edit User")
                },

                text = {

                    OutlinedTextField(
                        value = editedName,
                        onValueChange = {
                            editedName = it
                        },
                        label = {
                            Text("Full Name")
                        }
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(selectedUser!!.uid)
                                .update(
                                    "fullName",
                                    editedName
                                )
                                .addOnSuccessListener {

                                    val index = users.indexOfFirst {
                                        it.uid == selectedUser!!.uid
                                    }

                                    if (index != -1) {

                                        users[index] = users[index].copy(
                                            fullName = editedName
                                        )
                                    }

                                    Toast.makeText(
                                        context,
                                        "User updated",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    selectedUser = null
                                }
                        }
                    ) {

                        Text("Save")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            selectedUser = null
                        }
                    ) {

                        Text("Cancel")
                    }
                }
            )
        }  }
}