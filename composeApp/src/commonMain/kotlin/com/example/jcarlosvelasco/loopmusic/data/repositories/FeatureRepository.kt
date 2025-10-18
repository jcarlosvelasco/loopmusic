package com.example.jcarlosvelasco.loopmusic.data.repositories

import com.example.jcarlosvelasco.loopmusic.data.interfaces.SharedPreferencesInfrType
import com.example.jcarlosvelasco.loopmusic.data.utils.screenFeaturesKey
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetSelectedScreenFeaturesRepoType
import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetSelectedScreenFeaturesRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

class FeatureRepository(
    private val sharedPreferences: SharedPreferencesInfrType
):
    GetSelectedScreenFeaturesRepoType,
    SetSelectedScreenFeaturesRepoType
{
    private val separator = "++--"

    override fun getSelectedScreenFeatures(): List<SCREEN_FEATURES>? {
        val mode = sharedPreferences.getString(screenFeaturesKey) ?: return null

        return mode.split(separator)
            .mapNotNull { SCREEN_FEATURES.fromStorageString(it) }
            .takeIf { it.isNotEmpty() }
    }

    override fun setSelectedScreenFeatures(features: List<SCREEN_FEATURES>) {
        val mode = features.joinToString(separator) { it.toStorageString() }
        sharedPreferences.putString(screenFeaturesKey, mode)
    }
}