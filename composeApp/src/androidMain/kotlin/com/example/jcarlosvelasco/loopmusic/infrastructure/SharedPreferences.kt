package com.example.jcarlosvelasco.loopmusic.infrastructure

import android.content.Context
import android.content.SharedPreferences
import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import androidx.core.content.edit

class SharedPreferences(context: Context) : SharedPreferencesInfrType {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("loopMusic", Context.MODE_PRIVATE)

    override fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }
}