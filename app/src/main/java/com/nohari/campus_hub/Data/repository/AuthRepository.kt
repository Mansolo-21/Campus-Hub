package com.nohari.campus_hub.Data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            onError("All fields are required")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener

                val userMap = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "role" to "student"
                )

                db.collection("users")
                    .document(userId)
                    .set(userMap)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener {
                        onError(it.message ?: "Failed to save user")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Registration failed")
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Email and password required")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Login failed")
            }
    }

    fun logout() {
        auth.signOut()
    }
}