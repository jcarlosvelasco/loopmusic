package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.database.AppDatabase

expect class DatabaseFactory() {
    fun createDatabase(): AppDatabase
}
