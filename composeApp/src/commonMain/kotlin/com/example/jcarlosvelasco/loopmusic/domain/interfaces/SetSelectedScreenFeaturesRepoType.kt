package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

interface SetSelectedScreenFeaturesRepoType {
    fun setSelectedScreenFeatures(features: List<SCREEN_FEATURES>)
}