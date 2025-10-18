package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetListModeRepoType
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode

interface SetListModeType {
    fun execute(mode: ListMode)
}

class SetListMode(
    private val repo: SetListModeRepoType
): SetListModeType {
    override fun execute(mode: ListMode) {
        repo.setListMode(mode)
    }
}