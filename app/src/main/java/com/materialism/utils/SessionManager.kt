package com.materialism.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
  private const val PREF_NAME = "MaterialismSession"
  private const val KEY_USER_ID = "userId"
  private const val KEY_IS_LOGGED_IN = "isLoggedIn"

  private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
  }

  fun createSession(context: Context, userId: Int) {
    val editor = getPreferences(context).edit()
    editor.putInt(KEY_USER_ID, userId)
    editor.putBoolean(KEY_IS_LOGGED_IN, true)
    editor.apply()
  }

  fun isLoggedIn(context: Context): Boolean {
    return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
  }

  fun getUserId(context: Context): Int {
    return getPreferences(context).getInt(KEY_USER_ID, -1)
  }

  fun logout(context: Context) {
    val editor = getPreferences(context).edit()
    editor.clear()
    editor.apply()
  }
}
