package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.FilesInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.MediaPlayerInfrType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.PlaybackServiceType
import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import com.example.jcarlosvelasco.loopmusic.data.mapper.AlbumMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.ArtistMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.SongMapper
import com.example.jcarlosvelasco.loopmusic.data.mapper.SongWithArtistMapper
import com.example.jcarlosvelasco.loopmusic.data.utils.listModeKey
import com.example.jcarlosvelasco.loopmusic.data.utils.playModeKey
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.*
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.dao.SongDao
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode
import com.example.jcarlosvelasco.loopmusic.presentation.audio.PlayMode
import com.example.jcarlosvelasco.loopmusic.ui.features.playing.MediaState
import com.example.jcarlosvelasco.loopmusic.utils.log

class SongRepository(
    private val songDao: SongDao,
    private val songMapper: SongMapper,
    private val artistMapper: ArtistMapper,
    private val songWithArtistMapper: SongWithArtistMapper,
    private val albumMapper: AlbumMapper,
    private val files: FilesInfrType,
    private val mediaPlayer: MediaPlayerInfrType,
    private val sharedPreferences: SharedPreferencesInfrType,
    private val playbackService: PlaybackServiceType?
):
    CacheSongsRepositoryType,
    GetCachedSongsRepositoryType,
    DeleteSongsFromCacheRepoType,
    CleanUnusedArtworkRepoType,
    PlaySongRepoType,
    PauseSongRepoType,
    ResumeSongRepoType,
    SeekToRepoType,
    GetPlayModeRepoType,
    GetListModeRepoType,
    SetPlayModeRepoType,
    SetListModeRepoType,
    GetCurrentPlayerPositionRepoType,
    GetCurrentPlayerStateRepoType,
    PublishNowPlayingRepoType,
    UpdateElapsedRepoType,
    SetPlayingRepoType,
    PutSongInPlayerRepoType,
    SetOnSongEndCallbackRepoType
{
    //TODO: Better format
    private val listModeFormatted: Map<ListMode, String> = mapOf(
        ListMode.LOOP to "loop",
        ListMode.ONE_TIME to "oneTime",
        ListMode.ONE_SONG to "oneSong",
    )

    private val playModeFormatted: Map<PlayMode, String> = mapOf(
        PlayMode.SHUFFLE to "shuffle",
        PlayMode.ORDERED to "ordered",
    )

    init {
        log("SongRepository", "Init")
        log("SongRepository", "is playbackService null? ${playbackService == null}")
    }

    override suspend fun cacheSongs(list: List<Song>, artworkList: List<ByteArray?>) {
        //Store artwork
        for (i in 0 until list.size) {
            val hash = list[i].album.artworkHash
            val bytes = artworkList[i]

            if (!hash.isNullOrBlank() && bytes != null) {
                files.storeImageInFolder(bytes, hash, isExternal = false)
            }
        }

        //Store song info
        val artistEntities = list
            .map { artistMapper.mapToArtistEntity(it.artist) }

        val albumEntities = list
            .map { albumMapper.mapToAlbumEntity(it.album) }

        val artistIds = songDao.insertArtists(artistEntities)
        val albumIds = songDao.insertAlbums(albumEntities)

        val entities = list.mapIndexed { index, song ->
            songMapper.mapToSongEntity(song, artistIds[index], albumIds[index])
        }
        songDao.insertSongs(entities)
    }

    override suspend fun getCachedSongs(): List<Song> {
        val entities = songDao.getSongsWithArtist()
        val songs = entities.map {
            songWithArtistMapper.mapToSong(it)
        }
        return songs.map { song ->
            val hash = song.album.artworkHash
            val artwork = if (!hash.isNullOrBlank()) files.readCachedArtworkBytes(hash, fromSongs = true, isExternal = false) else null
            if (artwork != null) {
                //Add artwork to song album
                song.copy(album = song.album.copy(artwork = artwork))
            } else {
                song
            }
        }
    }

    override suspend fun deleteSongsFromCache(paths: List<String>) {
        songDao.deleteSongsByPaths(paths)
    }

    override suspend fun cleanUnusedArtwork() {
        val inDb = songDao.getAllArtworkHashesInDb().toSet()
        val inDisk = files.listCachedArtworkIdentifiers().toSet()
        val toRemove = inDisk - inDb
        toRemove.forEach { files.removeImageFromCache(it) }
    }

    override fun playSong(song: Song) {
        mediaPlayer.playSong(song)
    }

    override fun pauseSong() {
        mediaPlayer.pauseSong()
    }

    override fun resumeSong() {
        mediaPlayer.resumeSong()
    }

    override fun seekTo(progress: Float) {
        mediaPlayer.seekTo(progress)
    }

    override fun getListMode(): ListMode? {
        val mode = sharedPreferences.getString(listModeKey)
        log("SongRepository", "mode: $mode")
        return when (mode) {
            listModeFormatted[ListMode.LOOP] -> ListMode.LOOP
            listModeFormatted[ListMode.ONE_TIME] -> ListMode.ONE_TIME
            listModeFormatted[ListMode.ONE_SONG] -> ListMode.ONE_SONG
            else -> null
        }
    }

    override fun getPlayMode(): PlayMode? {
        val mode = sharedPreferences.getString(playModeKey)
        log("SongRepository", "mode: $mode")
        return when (mode) {
            playModeFormatted[PlayMode.SHUFFLE] -> PlayMode.SHUFFLE
            playModeFormatted[PlayMode.ORDERED] -> PlayMode.ORDERED
            else -> null
        }
    }

    override fun setListMode(mode: ListMode) {
        val itemToStore = listModeFormatted[mode]
        log("SongRepository", "itemToStore: $itemToStore")
        if (itemToStore != null) {
            sharedPreferences.putString(listModeKey, itemToStore)
        }
    }

    override fun setPlayMode(mode: PlayMode) {
        log("SongRepository", "setPlayMode: $mode")
        val itemToStore = playModeFormatted[mode]
        if (itemToStore != null) {
            sharedPreferences.putString(playModeKey, itemToStore)
        }
    }

    override fun getCurrentPlayerPosition(): Float {
        return mediaPlayer.getCurrentPosition()
    }

    override fun getCurrentPlayerState(): MediaState {
        return mediaPlayer.getCurrentState()
    }

    override fun publishNowPlaying(song: Song, positionMs: Long, isPlaying: Boolean) {
        playbackService?.publishNowPlaying(song, positionMs, isPlaying)
    }

    override fun updateElapsed(positionMs: Long, isPlaying: Boolean) {
        playbackService?.updateElapsed(positionMs, isPlaying)
    }

    override fun setPlaying(isPlaying: Boolean) {
        playbackService?.setPlaying(isPlaying)
    }

    override fun putSongInPlayer(song: Song, pos: Float) {
        mediaPlayer.putSongInPlayer(song, pos)
    }

    override fun setOnSongEndCallback(callback: () -> Unit) {
        mediaPlayer.setOnSongEndCallback(callback)
    }
}