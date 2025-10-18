package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.IOSStatusBarManager
import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType

actual class StatusBarFactory actual constructor() {
    actual fun getStatusBarManager(): StatusBarManagerType {
        return IOSStatusBarManager()
    }
}