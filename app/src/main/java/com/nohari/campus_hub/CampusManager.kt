package com.nohari.campus_hub

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CampusManager{

    fun getCampus(onResult: (Map<String, Any>?) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?:
        return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid).get()
            .addOnSuccessListener { userDoc ->
                val campusId = userDoc.getString("campusId")?:
                return@addOnSuccessListener

            db.collection("campuses").document(campusId).get()
                .addOnSuccessListener { onResult(it.data) }}
    }
}
