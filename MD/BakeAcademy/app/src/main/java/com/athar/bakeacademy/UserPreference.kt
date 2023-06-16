package com.athar.bakeacademy

import android.content.Context

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USERID = "userId"
        private const val TOKEN = "token"
        private const val NAMA = "nama"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val ALERGI = "alergi"
        private const val FOTO = "foto"
    }

    private val preference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = preference.edit()
        editor.putInt(USERID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.putString(NAMA, value.nama)
        editor.putString(USERNAME, value.username)
        editor.putString(EMAIL, value.email)
        editor.putString(ALERGI, value.alergi)
        editor.putString(FOTO, value.foto)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.userId = preference.getInt(USERID, 0)
        model.token = preference.getString(TOKEN, null)
        model.nama = preference.getString(NAMA, null)
        model.username = preference.getString(USERNAME, null)
        model.email = preference.getString(EMAIL, null)
        model.alergi = preference.getString(ALERGI, null)
        model.foto = preference.getString(FOTO, null)

        return model
    }

    fun removeUser() {
        val editor = preference.edit().clear()
        editor.apply()
    }

}