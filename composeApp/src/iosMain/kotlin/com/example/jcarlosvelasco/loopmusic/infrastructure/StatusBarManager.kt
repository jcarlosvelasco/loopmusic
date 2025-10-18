package com.example.jcarlosvelasco.loopmusic.infrastructure

import platform.UIKit.*

class IOSStatusBarManager : StatusBarManagerType {
    override fun setStatusBarStyle(isDark: Boolean) {
        val statusBarStyle = if (isDark) {
            UIStatusBarStyleLightContent
        } else {
            UIStatusBarStyleDarkContent
        }

        // For iOS 13+
        if (UIApplication.sharedApplication.windows.isNotEmpty()) {
            val window = UIApplication.sharedApplication.windows.first() as? UIWindow
            window?.overrideUserInterfaceStyle = if (isDark) {
                UIUserInterfaceStyle.UIUserInterfaceStyleDark
            } else {
                UIUserInterfaceStyle.UIUserInterfaceStyleLight
            }
        }

        // Configure status bar
        UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle, animated = true)
    }

    override fun isSystemInDarkTheme(): Boolean {
        return if (UIApplication.sharedApplication.windows.isNotEmpty()) {
            val window = UIApplication.sharedApplication.windows.first() as? UIWindow
            window?.traitCollection?.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
        } else {
            UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleLight
        }
    }
}