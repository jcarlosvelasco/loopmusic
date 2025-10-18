package com.example.jcarlosvelasco.loopmusic.helpers

fun capitalizeFirstLetter(text: String): String {
    return text.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}
