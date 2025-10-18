package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetListModeRepoType
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode

interface GetListModeType {
    fun execute(): ListMode?
}

class GetListMode(
    private val repo: GetListModeRepoType
): GetListModeType {
    override fun execute(): ListMode? {
        return repo.getListMode()
    }
}