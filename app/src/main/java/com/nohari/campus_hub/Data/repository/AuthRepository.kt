package com.nohari.campus_hub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // ✅ FIXED REGISTER
    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()

            val user = result.user ?: return Result.failure(Exception("User is null"))
            val uid = user.uid

            val userMap = hashMapOf(
                "uid" to uid,
                "fullName" to fullName,
                "email" to email,
                "role" to "student",
                "createdAt" to System.currentTimeMillis()
            )

            db.collection("users").document(uid).set(userMap).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ LOGIN (your version already good)
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email.trim(), password).await()

            val user = result.user ?: return Result.failure(Exception("User is null"))
            val uid = user.uid

            val userRef = db.collection("users").document(uid)
            val doc = userRef.get().await()

            if (!doc.exists()) {
                val newUser = hashMapOf(
                    "uid" to uid,
                    "email" to user.email,
                    "fullName" to (user.displayName ?: ""),
                    "role" to "student",
                    "createdAt" to System.currentTimeMillis()
                )

                userRef.set(newUser).await()
            }

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