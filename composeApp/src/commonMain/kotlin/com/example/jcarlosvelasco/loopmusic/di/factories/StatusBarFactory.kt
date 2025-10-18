package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.StatusBarManagerType

expect class StatusBarFactory() {
    fun getStatusBarManager(): StatusBarManagerType
}