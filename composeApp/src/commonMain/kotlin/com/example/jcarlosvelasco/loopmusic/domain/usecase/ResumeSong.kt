package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.ResumeSongRepoType

interface ResumeSongType {
    fun execute()
}

class ResumeSong(
    private val repo: ResumeSongRepoType
): ResumeSongType {
    override fun execute() {
        repo.resumeSong()
    }
}