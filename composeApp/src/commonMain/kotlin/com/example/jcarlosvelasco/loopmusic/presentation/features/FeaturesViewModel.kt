package com.example.jcarlosvelasco.loopmusic.presentation.features

import androidx.lifecycle.ViewModel
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES
import com.example.jcarlosvelasco.loopmusic.domain.model.SCREEN_FEATURES.Companion.toNavigationTab
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetScreenFeaturesType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.GetSelectedScreenFeaturesType
import com.example.jcarlosvelasco.loopmusic.domain.usecase.SetSelectedScreenFeaturesType
import com.example.jcarlosvelasco.loopmusic.ui.features.main.NavigationTab
import com.example.jcarlosvelasco.loopmusic.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//TODO: Find a way not to forget to add a new feature here


class FeaturesViewModel(
    private val getScreenFeatures: GetScreenFeaturesType,
    private val getSelectedScreenFeatures: GetSelectedScreenFeaturesType,
    private val setSelectedScreenFeatures: SetSelectedScreenFeaturesType
): ViewModel() {
    private var screenFeatures: Set<SCREEN_FEATURES>? = null

    private var _selectedScreenFeatures: MutableStateFlow<List<SCREEN_FEATURES>?> = MutableStateFlow(null)
    val selectedScreenFeatures = _selectedScreenFeatures.asStateFlow()

    private val defaultScreenFeatures = listOf(SCREEN_FEATURES.Home, SCREEN_FEATURES.Search, SCREEN_FEATURES.Songs)

    val remainingScreenFeatures: Set<SCREEN_FEATURES>?
        get() = screenFeatures?.minus((_selectedScreenFeatures.value ?: emptyList()).toSet())

    val selectedNavigationTabs: List<NavigationTab>
        get() = _selectedScreenFeatures.value?.map {
            it.toNavigationTab()
        } ?: emptyList()


    private val _selectedTab = MutableStateFlow<NavigationTab?>(null)
    var selectedTab = _selectedTab.asStateFlow()


    init {
        loadTabs()
    }

    private fun loadTabs() {
        screenFeatures = getScreenFeatures.execute().toSet()

        val selectedFeatures = getSelectedScreenFeatures.execute()
        log("FeaturesViewModel", "Selected features: $selectedFeatures")
        if (selectedFeatures == null) {
            _selectedScreenFeatures.value = defaultScreenFeatures
            setSelectedScreenFeatures.execute(defaultScreenFeatures)
        }
        else {
            _selectedScreenFeatures.value = selectedFeatures
        }

        selectedNavigationTabs.isNotEmpty().let {
            _selectedTab.value = selectedNavigationTabs.first()
        }
    }


    fun removeFeature(feature: SCREEN_FEATURES) {
        _selectedScreenFeatures.value = _selectedScreenFeatures.value?.minus(feature)
        log("FeaturesViewModel", "Selected features: ${_selectedScreenFeatures.value}")
        _selectedScreenFeatures.value?.let {
            setSelectedScreenFeatures.execute(it)
        }
    }

    fun addFeature(feature: SCREEN_FEATURES) {
        _selectedScreenFeatures.value = _selectedScreenFeatures.value?.plus(feature)
        log("FeaturesViewModel", "Selected features: ${_selectedScreenFeatures.value}")
        _selectedScreenFeatures.value?.let {
            setSelectedScreenFeatures.execute(it)
        }
    }

    fun moveFeatureLeft(feature: SCREEN_FEATURES) {
        val features = _selectedScreenFeatures.value?.toMutableList() ?: return
        val index = features.indexOf(feature)
        if (index > 0) {
            features[index] = features[index - 1]
            features[index - 1] = feature
        }
        _selectedScreenFeatures.value = features
        _selectedScreenFeatures.value?.let {
            setSelectedScreenFeatures.execute(it)
        }
    }

    fun moveFeatureRight(feature: SCREEN_FEATURES) {
        val features = _selectedScreenFeatures.value?.toMutableList() ?: return
        val index = features.indexOf(feature)
        if (index < features.size - 1) {
            features[index] = features[index + 1]
            features[index + 1] = feature
        }
        _selectedScreenFeatures.value = features
        _selectedScreenFeatures.value?.let {
            setSelectedScreenFeatures.execute(it)
        }
    }

    fun setNavigationTab(tab: NavigationTab) {
        _selectedTab.value = tab
    }
}