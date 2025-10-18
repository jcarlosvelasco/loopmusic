package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetCurrentPlayerPositionRepoType

interface GetCurrentMediaPlayerPositionType {
    fun execute(): Float
}

class GetCurrentMediaPlayerPosition(
    private val repo: GetCurrentPlayerPositionRepoType
): GetCurrentMediaPlayerPositionType {
    override fun execute(): Float {
        return repo.getCurrentPlayerPosition()
    }
}