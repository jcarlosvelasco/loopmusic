package com.example.jcarlosvelasco.loopmusic.domain.interfaces

import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES

interface GetSelectedScreenFeaturesRepoType {
    fun getSelectedScreenFeatures(): List<SCREEN_FEATURES>?
}