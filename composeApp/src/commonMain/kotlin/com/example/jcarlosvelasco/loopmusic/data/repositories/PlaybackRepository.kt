package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import com.example.jcarlosvelasco.loopmusic.data.utils.playbackStateKey
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetPlaybackStateRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SavePlaybackStateRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.PlaybackState
import kotlinx.serialization.json.Json

class PlaybackRepository(
    private val sharedPreferences: SharedPreferencesInfrType
):
    SavePlaybackStateRepoType,
    GetPlaybackStateRepoType
{
    private val json = Json { ignoreUnknownKeys = true }

    override fun savePlaybackState(state: PlaybackState) {
        val encodedState = json.encodeToString(state)
        sharedPreferences.putString(playbackStateKey, encodedState)
    }

    override fun getPlaybackState(): PlaybackState? {
        val encodedState = sharedPreferences.getString(playbackStateKey)
        if (encodedState != null) {
            try {
                return json.decodeFromString<PlaybackState>(encodedState)
            } catch (e: Exception) {
                println("Error decoding playback state: ${e.message}")
            }
        }
        return null
    }
}