package com.example.jcarlosvelasco.loopmusic.infrastructure

interface StatusBarManagerType {
    fun setStatusBarStyle(isDark: Boolean)
    fun isSystemInDarkTheme(): Boolean
}