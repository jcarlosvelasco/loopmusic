package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist

interface GetPlaylistFromPlaylistIdRepoType {
    suspend fun getPlaylistFromPlaylistId(playlistId: Long): Playlist?
}