package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.IOSSharedPreferences
import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType

actual class SharedPreferencesFactory {
    actual fun getSharedPreferences(): SharedPreferencesInfrType {
        return IOSSharedPreferences()
    }
}