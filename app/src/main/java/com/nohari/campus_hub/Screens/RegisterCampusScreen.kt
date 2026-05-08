package com.nohari.campus_hub.Screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterCampusScreen(
    navController : NavController
){
    val context =LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var schoolName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(("")) }
    var password by remember {mutableStateOf("")}
    var confirmPassword by remember { mutableStateOf("") }
    var logoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract =
            ActivityResultContracts.GetContent()) {
        uri -> logoUri =uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text("Register Campus",style =
        MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        Button(onClick = {launcher.launch("image/*") })
        {
            Text("Upload Image")
        }
        logoUri?.let {
            AsyncImage(model = it,
                contentDescription =null,modifier =
            Modifier.size(100.dp))
        }

        Spacer(Modifier.height(20.dp))

        Button(onclick = {
            if(password != confirmPassword){
                Toast.makeText(context, "Passwords Don't Match", Toast.LENGTH_SHORT).show()
                return@Button
            }
            if(logoUri == null){
                Toast.makeText(context,"Upload logo",Toast.LENGTH_SHORT).show()
                return@Button
            }

            MediaManager.get().upload(logoUri)
                .unsigned("campus_hub_upload")
                .callback(object : UploadCallback{
                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val logoUrl =resultData["secure_url"].toString()

                        auth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener {
                                val uid = auth.currentUser!! uid
                                val campusId = db.collection("campuses").document().id

                                val campus = hashMapOf(
                                    "id" to campusId,
                                    "name" to schoolName,
                                    "logoUrl" to logoUrl,
                                    "images" to listOf<String>()
                                )
                                db.collection("campuses").document(campusId).set(campus)
                                val user = hashMapOf(
                                    "uid" to uid,
                                    "email" to email,
                                    "role" to "admin",
                                    "campusId" to campusId
                                )
                                db.collection("users").document(uid).set(user)
                                navController.navigate(home)
                            }
                    }
                    override fun onStart(requestId: String?)
                })
        })
    }
}