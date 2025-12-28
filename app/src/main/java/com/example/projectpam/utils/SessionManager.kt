package com.example.projectpam.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    fun saveSession(token: String, role: String) {
        prefs.edit()
            .putString("TOKEN", token)
            .putString("ROLE", role)
            .apply()
    }

    fun getToken(): String? = prefs.getString("TOKEN", null)

    fun getRole(): String? = prefs.getString("ROLE", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
