package com.nohari.campus_hub.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.navigation.Routes

@Composable
fun RegisterCampusScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var schoolName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var logoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        logoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Register Campus",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = schoolName,
            onValueChange = { schoolName = it },
            label = { Text("School Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Admin Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Upload Campus Logo")
        }

        Spacer(modifier = Modifier.height(12.dp))

        logoUri?.let {

            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (schoolName.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty()
                ) {

                    Toast.makeText(
                        context,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                if (password != confirmPassword) {

                    Toast.makeText(
                        context,
                        "Passwords don't match",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                if (logoUri == null) {

                    Toast.makeText(
                        context,
                        "Upload logo",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

                MediaManager.get()
                    .upload(logoUri)
                    .unsigned("campus_hub_upload")
                    .callback(object : UploadCallback {

                        override fun onStart(requestId: String?) {}

                        override fun onProgress(
                            requestId: String?,
                            bytes: Long,
                            totalBytes: Long
                        ) {}

                        override fun onSuccess(
                            requestId: String?,
                            resultData: Map<*, *>?
                        ) {

                            val logoUrl =
                                resultData?.get("secure_url") as? String ?: ""

                            auth.createUserWithEmailAndPassword(
                                email,
                                password
                            ).addOnSuccessListener {

                                val uid =
                                    auth.currentUser!!.uid

                                val campusId =
                                    db.collection("campuses")
                                        .document()
                                        .id

                                val campus = hashMapOf(
                                    "id" to campusId,
                                    "name" to schoolName,
                                    "logoUrl" to logoUrl,
                                    "images" to listOf<String>()
                                )

                                db.collection("campuses")
                                    .document(campusId)
                                    .set(campus)

                                val user = hashMapOf(
                                    "uid" to uid,
                                    "email" to email,
                                    "role" to "admin",
                                    "campusId" to campusId
                                )

                                db.collection("users")
                                    .document(uid)
                                    .set(user)

                                Toast.makeText(
                                    context,
                                    "Campus Created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(Routes.REGISTER_CAMPUS) { inclusive = true }
                                }
                            }
                        }

                        override fun onError(
                            requestId: String?,
                            error: ErrorInfo?
                        ) {

                            Toast.makeText(
                                context,
                                "Upload Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onReschedule(
                            requestId: String?,
                            error: ErrorInfo?
                        ) {}
                    })
                    .dispatch()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Create Campus")
        }
    }
}