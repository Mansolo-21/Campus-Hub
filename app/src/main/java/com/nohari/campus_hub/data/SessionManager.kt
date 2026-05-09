package com.nohari.campus_hub.data

import android.content.Context

object SessionManager {

    private const val PREF_NAME = "campus_session"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"
    private const val KEY_CAMPUS_SELECTED = "campus_selected"

    fun setOnboardingDone(context: Context, done: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, done).apply()
    }

    fun isOnboardingDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    }

    fun setCampusSelected(context: Context, selected: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_CAMPUS_SELECTED, selected).apply()
    }

    fun isCampusSelected(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_CAMPUS_SELECTED, false)
    }
}