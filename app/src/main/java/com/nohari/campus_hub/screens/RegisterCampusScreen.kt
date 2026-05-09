package com.nohari.campus_hub.screens

import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.data.SessionManager
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

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    var logoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        logoUri = uri
    }

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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.Center),

            shape = RoundedCornerShape(32.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Create Campus",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Setup your smart campus platform",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // LOGO SECTION

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape),

                            shape = CircleShape
                        ) {

                            if (logoUri != null) {

                                AsyncImage(
                                    model = logoUri,
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
                                        imageVector =
                                            Icons.Default.AddPhotoAlternate,

                                        contentDescription = null,

                                        tint = Color.Gray,

                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = {
                                launcher.launch("image/*")
                            }
                        ) {

                            Text("Upload Campus Logo")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // CAMPUS NAME

                OutlinedTextField(
                    value = schoolName,

                    onValueChange = {
                        schoolName = it.trimStart()
                    },

                    label = { Text("Campus Name") },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // EMAIL

                OutlinedTextField(
                    value = email,

                    onValueChange = {
                        email = it.trim()
                    },

                    label = { Text("Admin Email") },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // PASSWORD

                OutlinedTextField(
                    value = password,

                    onValueChange = {
                        password = it.trim()
                    },

                    label = { Text("Password") },

                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription = null
                            )
                        }
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // CONFIRM PASSWORD

                OutlinedTextField(
                    value = confirmPassword,

                    onValueChange = {
                        confirmPassword = it.trim()
                    },

                    label = { Text("Confirm Password") },

                    visualTransformation =
                        if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                confirmPasswordVisible =
                                    !confirmPasswordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (confirmPasswordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,

                                contentDescription = null
                            )
                        }
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // CREATE BUTTON

                Button(
                    onClick = {

                        loading = true

                        val cleanSchoolName = schoolName.trim()
                        val cleanEmail = email.trim()
                        val cleanPassword = password.trim()
                        val cleanConfirmPassword = confirmPassword.trim()

                        // ---------------- VALIDATION ----------------
                        if (
                            cleanSchoolName.isBlank() ||
                            cleanEmail.isBlank() ||
                            cleanPassword.isBlank() ||
                            cleanConfirmPassword.isBlank()
                        ) {
                            loading = false
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
                            loading = false
                            Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (cleanPassword.length < 6) {
                            loading = false
                            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (cleanPassword != cleanConfirmPassword) {
                            loading = false
                            Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (logoUri == null) {
                            loading = false
                            Toast.makeText(context, "Upload campus logo", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // ---------------- CLOUDINARY UPLOAD ----------------
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

                                    val logoUrl = resultData?.get("secure_url") as? String

                                    if (logoUrl == null) {
                                        loading = false
                                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                        return
                                    }

                                    // ---------------- CREATE AUTH USER ----------------
                                    auth.createUserWithEmailAndPassword(
                                        cleanEmail,
                                        cleanPassword
                                    ).addOnSuccessListener {

                                        val uid = auth.currentUser?.uid ?: return@addOnSuccessListener
                                        val campusId = db.collection("campuses").document().id

                                        val campus = hashMapOf(
                                            "id" to campusId,
                                            "campusName" to cleanSchoolName,
                                            "logoUrl" to logoUrl
                                        )

                                        // ---------------- SAVE CAMPUS ----------------
                                        db.collection("campuses")
                                            .document(campusId)
                                            .set(campus)
                                            .addOnSuccessListener {

                                                val user = hashMapOf(
                                                    "uid" to uid,
                                                    "email" to cleanEmail,
                                                    "role" to "admin",
                                                    "campusId" to campusId
                                                )

                                                // ---------------- SAVE USER ----------------
                                                db.collection("users")
                                                    .document(uid)
                                                    .set(user)
                                                    .addOnSuccessListener {

                                                        loading = false

                                                        // 🔥 IMPORTANT: mark session
                                                        SessionManager.setOnboardingDone(context, true)
                                                        SessionManager.setCampusSelected(context, true)

                                                        Toast.makeText(
                                                            context,
                                                            "Campus Created Successfully",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                        // ---------------- NAVIGATE ----------------
                                                        navController.navigate(Routes.LOGIN) {
                                                            popUpTo(Routes.REGISTER_CAMPUS) {
                                                                inclusive = true
                                                            }
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        loading = false
                                                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                                    }
                                            }
                                            .addOnFailureListener {
                                                loading = false
                                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                            }

                                    }.addOnFailureListener {
                                        loading = false
                                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onError(
                                    requestId: String?,
                                    error: ErrorInfo?
                                ) {
                                    loading = false
                                    Toast.makeText(
                                        context,
                                        error?.description ?: "Image upload failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onReschedule(
                                    requestId: String?,
                                    error: ErrorInfo?
                                ) {}
                            })
                            .dispatch()
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text(
                            text = "Create Campus",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                    }
                }
            }
        }
