package com.nohari.campus_hub.Data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavHostController
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Item
import java.util.HashMap

class ItemViewModel(
    private val navController: NavHostController,
    private val context: Context
) {

    private val db = FirebaseFirestore.getInstance()

    init {
        val config = HashMap<String, String>()
        config["cloud_name"] = "YOUR_CLOUD_NAME"
        config["api_key"] = "YOUR_API_KEY"
        config["api_secret"] = "YOUR_API_SECRET"

        try {
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // prevents crash if already initialized
        }
    }

    fun uploadItem(
        imageUri: Uri?,
        name: String,
        price: String,
        description: String
    ) {

        if (imageUri == null) {
            Toast.makeText(context, "Select image", Toast.LENGTH_SHORT).show()
            return
        }

        if (name.isBlank() || price.isBlank() || description.isBlank()) {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val itemId = db.collection("items").document().id

        MediaManager.get().upload(imageUri)
            .callback(object : UploadCallback {

                override fun onStart(requestId: String?) {}

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {

                    val imageUrl = resultData["secure_url"].toString()

                    val item = Item(
                        id = itemId,
                        name = name,
                        price = price,
                        description = description,
                        imageUrl = imageUrl
                    )

                    db.collection("items")
                        .document(itemId)
                        .set(item)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(context, error?.description, Toast.LENGTH_SHORT).show()
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }

    fun getItems(items: MutableList<Item>) {
        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                items.clear()
                for (doc in result) {
                    items.add(doc.toObject(Item::class.java))
                }
            }
    }

    fun deleteItem(id: String) {
        db.collection("items").document(id).delete()
    }
}