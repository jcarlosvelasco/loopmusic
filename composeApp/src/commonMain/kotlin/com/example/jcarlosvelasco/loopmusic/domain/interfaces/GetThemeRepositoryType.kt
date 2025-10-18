package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Theme

interface GetThemeRepositoryType {
    fun getTheme(): Theme?
}