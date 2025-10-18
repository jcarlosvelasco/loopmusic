package com.example.jcarlosvelasco.loopmusic.utils

inline fun <T1, T2, R> let2(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
