package com.example.jcarlosvelasco.loopmusic.data.mapper

import com.example.jcarlosvelasco.loopmusic.domain.model.*
import com.example.jcarlosvelasco.loopmusic.helpers.getAlbumID
import com.example.jcarlosvelasco.loopmusic.helpers.getArtistID
import com.example.jcarlosvelasco.loopmusic.utils.sha256

class AudioMetadataMapper {
    fun mapToSong(file: File, data: AudioMetadata): Song {
        return Song(
            path = file.path,
            name = data.title ?: "Unknown title",
            artist = mapToArtist(data.artist),
            modificationDate = file.modificationDate,
            album = mapToAlbum(data, mapToArtist(data.albumArtist), data.artwork),
            duration = data.duration ?: 0,
            trackNumber = formatTrackNumber(data.trackNumber)
        )
    }

    fun formatTrackNumber(data: String?): Int? {
        //If string only contains numbers, return the number
        if (data?.matches(Regex("^[0-9]+$")) == true) {
            return data.toInt()
        }

        //If string contains number, hyphen and number, return the first number
        if (data?.matches(Regex("^[0-9]+-[0-9]+$")) == true) {
            return data.split("-")[0].toInt()
        }

        return null
    }

    fun formatYear(data: String?): Int? {
        //If data comes as a date, in format YYYY-MM-DD, return the year
        if (data?.matches(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) == true) {
            return data.split("-")[0].toInt()
        }

        //If data comes as a date, in format DD-MM-YYYY, return the year
        if (data?.matches(Regex("^[0-9]{2}-[0-9]{2}-[0-9]{4}$")) == true) {
            return data.split("-")[2].toInt()
        }

        //If data comes as a date, in format YYYYMMDD, return the year
        if (data?.matches(Regex("^[0-9]{4}[0-9]{2}[0-9]{2}$")) == true) {
            return data.take(4).toInt()
        }

        if (data?.matches(Regex("^[0-9]{4}/[0-9]{2}/[0-9]{2}$")) == true) {
            return data.split("/")[0].toInt()
        }

        //If data comes as a date, in format DD-MM-YYYY, return the year
        if (data?.matches(Regex("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) == true) {
            return data.split("/")[2].toInt()
        }

        return data?.toIntOrNull()
    }

    fun mapToAlbum(data: AudioMetadata, artist: Artist, artwork: ByteArray?): Album {
        return Album(
            id = getAlbumID(albumName = data.album, artistName = data.albumArtist),
            name = data.album ?: "Unknown album",
            artist = artist,
            artwork = artwork,
            artworkHash = createHash(artwork),
            year = if (data.year != null) formatYear(data.year) else formatYear(data.date)
        )
    }

    fun createHash(artwork: ByteArray?): String? {
        return artwork?.let { bytes -> sha256(bytes) }
    }


    fun mapToArtist(data: String?): Artist {
        return Artist(
            id = getArtistID(data),
            name = data ?: "Unknown artist"
        )
    }
}