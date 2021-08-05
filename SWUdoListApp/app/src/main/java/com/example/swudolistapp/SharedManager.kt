package com.example.swudolistapp

import android.content.Context
import android.content.SharedPreferences
import com.example.swudolistapp.PreferenceHelper.set
import com.example.swudolistapp.PreferenceHelper.get

// SharedManager를 통해 SharedPreference를 이용

class SharedManager(context: Context) {

    val prefs: SharedPreferences = PreferenceHelper.defultPrefs(context)

    fun saveCurrentUser(user: User){
        prefs["id"] = user.id
        prefs["pw"] = user.pw
        prefs["email"] = user.email
        prefs["subjects"] = user.subjects
    }

    fun getCurrentUser(): User{
        return User().apply {
            id = prefs["id", ""]
            pw = prefs["pw", ""]
            email = prefs["email", ""]
            subjects = prefs["subjects", ""]
        }
    }
}