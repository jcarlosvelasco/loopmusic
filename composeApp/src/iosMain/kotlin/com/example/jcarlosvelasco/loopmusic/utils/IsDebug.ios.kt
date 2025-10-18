package com.example.jcarlosvelasco.loopmusic.utils

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val IS_DEBUG: Boolean = run {
    var debug = false
    assert(run { debug = true; true })

    debug
}