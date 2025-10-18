package com.example.jcarlosvelasco.loopmusic.utils

fun cleanStringForComparison(str: String): String {
    return str
        .replace(Regex("[\\p{C}\\p{Z}&&[^ ]]"), "")
        .replace(Regex("^\\s+"), "")
        .replace(Regex("\\s+$"), "")
        .replace(Regex("\\s+"), " ")
}