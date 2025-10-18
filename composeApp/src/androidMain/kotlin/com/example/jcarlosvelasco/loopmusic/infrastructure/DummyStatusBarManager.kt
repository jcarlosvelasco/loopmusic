package com.example.jcarlosvelasco.loopmusic.infrastructure

class DummyStatusBarManager: StatusBarManagerType {
    override fun setStatusBarStyle(isDark: Boolean) {

    }

    override fun isSystemInDarkTheme(): Boolean {
        return false
    }
}