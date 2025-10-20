package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import kotlin.test.Test
import kotlin.test.assertEquals

class SongMapperTest {

    private val mapper = SongMapper()

    @Test
    fun `mapToSongEntity maps song correctly with all fields`() {
        val artist = Artist(id = 5, name = "The Beatles")
        val album = Album(
            id = 10,
            name = "Abbey Road",
            artist = artist,
            artworkHash = "hash123",
            year = 1969
        )

        val song = Song(
            path = "/music/beatles/abbey_road/come_together.mp3",
            name = "Come Together",
            artist = artist,
            modificationDate = 1234567890L,
            album = album,
            duration = 259000L,
            trackNumber = 1
        )

        val entity = mapper.mapToSongEntity(song, artistID = 5, albumID = 10)

        assertEquals("/music/beatles/abbey_road/come_together.mp3", entity.path)
        assertEquals("Come Together", entity.name)
        assertEquals(5, entity.artistId)
        assertEquals(10, entity.albumId)
        assertEquals(1234567890L, entity.modificationDate)
        assertEquals(259000L, entity.duration)
        assertEquals(1, entity.trackNumber)
    }

    @Test
    fun `mapToSongEntity maps song without track number`() {
        val artist = Artist(id = 1, name = "Unknown Artist")
        val album = Album(
            id = 2,
            name = "Unknown Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val song = Song(
            path = "/music/unknown.mp3",
            name = "Unknown Song",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 180000L,
            trackNumber = null
        )

        val entity = mapper.mapToSongEntity(song, artistID = 1, albumID = 2)

        assertEquals(null, entity.trackNumber)
        assertEquals(1, entity.artistId)
        assertEquals(2, entity.albumId)
    }

    @Test
    fun `mapToSongEntity handles different durations`() {
        val artist = Artist(id = 1, name = "Test Artist")
        val album = Album(
            id = 1,
            name = "Test Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val shortSong = Song(
            path = "/short.mp3",
            name = "Short",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 30000L,
            trackNumber = null
        )

        val longSong = Song(
            path = "/long.mp3",
            name = "Long",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 600000L,
            trackNumber = null
        )

        val shortEntity = mapper.mapToSongEntity(shortSong, artistID = 1, albumID = 1)
        val longEntity = mapper.mapToSongEntity(longSong, artistID = 1, albumID = 1)

        assertEquals(30000L, shortEntity.duration)
        assertEquals(600000L, longEntity.duration)
    }

    @Test
    fun `mapToSongEntity preserves path as primary key`() {
        val artist = Artist(id = 1, name = "Artist")
        val album = Album(
            id = 1,
            name = "Album",
            artist = artist,
            artworkHash = null,
            year = null
        )

        val song1 = Song(
            path = "/path/to/song1.mp3",
            name = "Song 1",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 180000L,
            trackNumber = 1
        )

        val song2 = Song(
            path = "/path/to/song2.mp3",
            name = "Song 2",
            artist = artist,
            modificationDate = 0L,
            album = album,
            duration = 180000L,
            trackNumber = 2
        )

        val entity1 = mapper.mapToSongEntity(song1, artistID = 1, albumID = 1)
        val entity2 = mapper.mapToSongEntity(song2, artistID = 1, albumID = 1)

        assertEquals("/path/to/song1.mp3", entity1.path)
        assertEquals("/path/to/song2.mp3", entity2.path)
    }
}
