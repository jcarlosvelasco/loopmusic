package com.example.jcarlosvelasco.loopmusic.data.interfaces

interface SharedPreferencesInfrType {
    fun getBooleanValue(key: String): Boolean
    fun putString(key: String, value: String)
    fun getString(key: String): String?
}