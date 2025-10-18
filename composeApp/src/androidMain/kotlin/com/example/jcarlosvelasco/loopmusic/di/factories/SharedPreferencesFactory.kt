package com.example.jcarlosvelasco.loopmusic.di.factories

import android.content.Context
import com.example.jcarlosvelasco.loopmusic.infrastructure.SharedPreferences
import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual class SharedPreferencesFactory: KoinComponent {
    actual fun getSharedPreferences(): SharedPreferencesInfrType {
        val context: Context = get()
        return SharedPreferences(context = context)
    }
}