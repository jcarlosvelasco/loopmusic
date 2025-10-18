package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetThemeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme

interface SetThemeType {
    fun execute(theme: Theme)
}

class SetTheme(
    private val setThemeRepo: SetThemeRepositoryType
) : SetThemeType {
    override fun execute(theme: Theme) {
        setThemeRepo.setTheme(theme)
    }
}