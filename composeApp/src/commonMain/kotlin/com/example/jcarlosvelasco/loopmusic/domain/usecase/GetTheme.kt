package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetThemeRepositoryType
import com.example.jcarlosvelasco.loopmusic.domain.model.Theme

interface GetThemeType {
    fun execute(): Theme?
}

class GetTheme(
    private val getThemeRepo: GetThemeRepositoryType
) : GetThemeType {
    override fun execute(): Theme? {
        return getThemeRepo.getTheme()
    }
}