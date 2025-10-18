package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

interface GetScreenFeaturesType {
    fun execute(): List<SCREEN_FEATURES>
}

class GetScreenFeatures(): GetScreenFeaturesType {
    override fun execute(): List<SCREEN_FEATURES> {
        return SCREEN_FEATURES.all()
    }
}