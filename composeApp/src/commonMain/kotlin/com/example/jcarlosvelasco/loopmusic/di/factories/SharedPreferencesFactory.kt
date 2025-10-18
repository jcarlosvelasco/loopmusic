package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType

expect class SharedPreferencesFactory() {
    fun getSharedPreferences(): SharedPreferencesInfrType
}