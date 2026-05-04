package com.nohari.campus_hub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        onSuccess:() -> Unit,
        onError: (String) -> Unit
    ){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val uid =auth.currentUser?.uid ?:
                return@addOnSuccessListener

                val user = hashMapOf(
                    "uid" to uid,
                    "fullName" to fullName,
                    "email" to email,
                    "role" to "student"
                )

                db.collection("users").document(uid).set(user)
                    .addOnSuccessListener{ onSuccess() }
           }
            .addOnFailureListener {
                onError(it.message?: "Error")
            }}
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email.trim(), password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}