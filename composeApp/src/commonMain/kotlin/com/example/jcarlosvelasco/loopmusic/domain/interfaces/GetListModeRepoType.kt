package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode

interface GetListModeRepoType {
    fun getListMode(): ListMode?
}