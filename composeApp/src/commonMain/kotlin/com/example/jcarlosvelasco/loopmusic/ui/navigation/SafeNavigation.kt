package com.example.jcarlosvelasco.loopmusic.ui.navigation

import androidx.navigation.NavController
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

private var lastNavMark: TimeMark? = null
private val NAVIGATION_DEBOUNCE_TIME = 500.milliseconds

fun safeNavigate(navController: NavController, destination: NavigationRoute) {
    val canNavigate = lastNavMark?.let { it.elapsedNow() >= NAVIGATION_DEBOUNCE_TIME } ?: true
    if (canNavigate) {
        lastNavMark = TimeSource.Monotonic.markNow()
        navController.navigate(destination)
    }
}

fun safePopBackStack(navController: NavController) {
    val canNavigate = lastNavMark?.let { it.elapsedNow() >= NAVIGATION_DEBOUNCE_TIME } ?: true
    if (canNavigate) {
        lastNavMark = TimeSource.Monotonic.markNow()
        navController.popBackStack()
    }
}