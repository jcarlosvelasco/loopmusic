package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.mapper.PlaylistMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.PlaylistWithSongsMapper
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.AddSongsToPlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.DeletePlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaylistFromPlaylistIdRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaylistsRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.RemoveSongFromPlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.RenamePlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.StorePlaylistRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.Playlist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.domain.usecase.RemoveSongFromPlaylistType
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.PlaylistDao
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.entities.PlaylistSongCrossRef
import com.example.jcarlosvelasco.loopmusic.utils.log

class PlaylistRepository(
    private val playlistDao: PlaylistDao,
    private val playlistWithSongsMapper: PlaylistWithSongsMapper,
    private val playlistMapper: PlaylistMapper,
) :
    GetPlaylistsRepoType,
    StorePlaylistRepoType,
    GetPlaylistFromPlaylistIdRepoType,
    AddSongsToPlaylistRepoType,
    DeletePlaylistRepoType,
    RenamePlaylistRepoType,
    RemoveSongFromPlaylistRepoType
{
    override suspend fun getPlaylists(): List<Playlist> {
        val playlistsWithSongs = playlistDao.getAllPlaylistsWithSongs()

        return playlistsWithSongs.map {
            playlistWithSongsMapper.mapToPlaylist(
                playlist = it
            )
        }
    }

    override suspend fun storePlaylist(playlist: Playlist): Long {
        val entity = playlistMapper.mapToPlaylistEntity(playlist)
        return playlistDao.insertPlaylist(playlist = entity)
    }

    override suspend fun getPlaylistFromPlaylistId(playlistId: Long): Playlist? {
        log("PlaylistRepository", "getPlaylistFromPlaylistId: $playlistId")
        playlistDao.getPlaylistWithSongs(playlistId)?.let {
            log("PlaylistRepository", "found playlist: $it")

            return playlistWithSongsMapper.mapToPlaylist(
                playlist = it
            )
        }

        return null
    }

    override suspend fun addSongsToPlaylist(songs: Set<Song>, playlistId: Long) {
        val crossRefs: List<PlaylistSongCrossRef> = songs.toList().map {
            //TODO: Improve position
            PlaylistSongCrossRef(
                songPath = it.path,
                playlistId = playlistId,
                position = 0
            )
        }
        playlistDao.insertPlaylistSongs(crossRefs)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylistById(playlistId)
    }

    override suspend fun renamePlaylist(playlistId: Long, playlistName: String) {
        playlistDao.renamePlaylist(playlistId, playlistName)
        log("PlaylistRepository", "Renamed playlist: $playlistId to $playlistName")
    }

    override suspend fun removeSongFromPlaylist(songPath: String, playlistId: Long) {
        //TODO: Improve position
        val crossRef = PlaylistSongCrossRef(songPath = songPath, playlistId = playlistId, position = 0)
        playlistDao.removeSongFromPlaylist(listOf(crossRef))
    }
}