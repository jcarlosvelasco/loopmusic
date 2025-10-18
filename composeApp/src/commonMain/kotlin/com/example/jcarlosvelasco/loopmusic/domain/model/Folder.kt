package com.example.jcarlosvelasco.loopmusic.domain.model

import kotlinx.serialization.Serializable

data class Folder(
    val path: String,
    val name: String,
    var subfolders: List<Folder> = emptyList(),
    var isExpanded: Boolean = false,
    var selectionState: SelectionState = SelectionState.UNSELECTED,
    val rootParent: Folder? = null
)

@Serializable
enum class SelectionState {
    UNSELECTED,
    SELECTED,
    PARTIAL
}