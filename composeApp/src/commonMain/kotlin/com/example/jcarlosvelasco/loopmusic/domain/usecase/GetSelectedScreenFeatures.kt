package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GetSelectedScreenFeaturesRepoType
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

interface GetSelectedScreenFeaturesType {
    fun execute(): List<SCREEN_FEATURES>?
}

class GetSelectedScreenFeatures(
    private val repo: GetSelectedScreenFeaturesRepoType
): GetSelectedScreenFeaturesType {
    override fun execute(): List<SCREEN_FEATURES>? {
        return repo.getSelectedScreenFeatures()
    }
}