package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.PauseSongRepoType

interface PauseSongType {
    fun execute()
}

class PauseSong(
    private val repo: PauseSongRepoType
): PauseSongType {
    override fun execute() {
        repo.pauseSong()
    }
}