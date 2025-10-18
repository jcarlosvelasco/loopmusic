package com.example.jcarlosvelasco.loopmusic.infrastructure

import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import platform.Foundation.NSUserDefaults

class IOSSharedPreferences: SharedPreferencesInfrType {
    override fun getBooleanValue(key: String): Boolean {
        val defaults = NSUserDefaults.standardUserDefaults()
        return defaults.boolForKey(key)
    }

    override fun getString(key: String): String? {
        val defaults = NSUserDefaults.standardUserDefaults()
        return defaults.stringForKey(key)
    }

    override fun putString(key: String, value: String) {
        val defaults = NSUserDefaults.standardUserDefaults()
        defaults.setObject(value, forKey = key)
    }
}