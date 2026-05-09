package com.nohari.campus_hub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // ✅ REGISTER (ALL USERS: student / teacher / admin)
    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String,
        role: String = "student"
    ): Result<Unit> {

        return try {

            val result =
                auth.createUserWithEmailAndPassword(email.trim(), password.trim()).await()

            val uid = result.user?.uid
                ?: return Result.failure(Exception("User creation failed"))

            val userMap = hashMapOf(
                "uid" to uid,
                "fullName" to fullName.trim(),
                "email" to email.trim(),
                "role" to role,
                "createdAt" to System.currentTimeMillis()
            )

            db.collection("users")
                .document(uid)
                .set(userMap)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ LOGIN (CLEAN + SAFE)
    suspend fun login(email: String, password: String): Result<Unit> {

        return try {

            val result =
                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password.trim()
                ).await()

            val uid = result.user?.uid
                ?: return Result.failure(Exception("Login failed"))

            val doc =
                db.collection("users")
                    .document(uid)
                    .get()
                    .await()

            if (!doc.exists()) {
                return Result.failure(Exception("User profile missing"))
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}