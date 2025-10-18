package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import com.example.jcarlosvelasco.loopmusic.data.utils.themeKey
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetThemeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetThemeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme

class ThemeRepository(
    private val sharedPreferencesType: SharedPreferencesInfrType
):
    GetThemeRepositoryType,
    SetThemeRepositoryType
{
    override fun getTheme(): Theme? {
        return sharedPreferencesType.getString(themeKey)?.let {
            Theme.valueOf(it)
        }
    }

    override fun setTheme(theme: Theme) {
        val stringTheme = theme.toString()
        sharedPreferencesType.putString(themeKey, stringTheme)
    }
}