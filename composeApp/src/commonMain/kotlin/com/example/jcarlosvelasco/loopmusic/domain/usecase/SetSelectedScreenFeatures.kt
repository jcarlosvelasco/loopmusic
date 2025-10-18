package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.SetSelectedScreenFeaturesRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

interface SetSelectedScreenFeaturesType {
    fun execute(features: List<SCREEN_FEATURES>)
}

class SetSelectedScreenFeatures(
    private val repo: SetSelectedScreenFeaturesRepoType
): SetSelectedScreenFeaturesType {
    override fun execute(features: List<SCREEN_FEATURES>) {
        repo.setSelectedScreenFeatures(features)
    }
}