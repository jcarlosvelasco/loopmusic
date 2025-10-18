package com.example.jcarlosvelasco.loopmusic.presentation.main

import com.example.jcarlosvelasco.loopmusic.domain.model.Album
import com.example.jcarlosvelasco.loopmusic.domain.model.Artist
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.utils.cleanStringForComparison

val songComparator: Comparator<Song> = compareBy(String.CASE_INSENSITIVE_ORDER) { song: Song ->
    cleanStringForComparison(song.name)
}

val albumComparator: Comparator<Album> = compareBy(String.CASE_INSENSITIVE_ORDER) { album: Album ->
    cleanStringForComparison(album.name)
}

val artistComparator: Comparator<Artist> = compareBy(String.CASE_INSENSITIVE_ORDER) { artist: Artist ->
    cleanStringForComparison(artist.name)
}