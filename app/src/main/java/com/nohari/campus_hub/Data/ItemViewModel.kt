package com.nohari.campus_hub.Data

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.nohari.campus_hub.models.Item

class ItemViewModel(
    private val navController: NavHostController,
    private val context: Context
) {

    private val db = FirebaseFirestore.getInstance()

    fun uploadItem(
        imageUri: Uri?,
        name: String,
        price: String,
        description: String
    ) {
        val id = db.collection("items").document().id

        val item = Item(
            id = id,
            name = name,
            price = price,
            description = description,
            imageUrl = imageUri.toString() // (temporary, replace later with Firebase Storage)
        )

        db.collection("items")
            .document(id)
            .set(item)
    }

    fun getItems(items: MutableList<Item>) {
        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                items.clear()
                for (doc in result) {
                    val item = doc.toObject(Item::class.java)
                    items.add(item)
                }
            }
    }

    fun deleteItem(id: String) {
        db.collection("items").document(id).delete()
    }
}