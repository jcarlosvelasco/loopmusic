package com.example.jcarlosvelasco.loopmusic.presentation.home

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.Song
import com.example.jcarlosvelasco.loopmusic.presentation.audio.ListMode
import com.example.jcarlosvelasco.loopmusic.utils.log

class HomeScreenViewModel(

): ViewModel() {

    fun formatUpcomingPlaylist(list: List<Song>, index: Int, max: Int, listMode: ListMode): List<Song> {
        log("HomeScreenViewModeL", "formatUpcomingPlaylist")
        if (listMode == ListMode.ONE_SONG) return emptyList()

        val remaining = list.drop(index + 1).take(max).toMutableList()

        if (listMode == ListMode.LOOP && remaining.size < max) {
            val needed = max - remaining.size
            remaining += list.take(needed)
        }

        return remaining
    }
}