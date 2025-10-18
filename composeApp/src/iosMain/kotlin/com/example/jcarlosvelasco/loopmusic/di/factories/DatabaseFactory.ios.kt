package com.example.jcarlosvelasco.loopmusic.di.factories

import com.example.jcarlosvelasco.loopmusic.infrastructure.Database
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.AppDatabase

actual class DatabaseFactory {
    actual fun createDatabase(): AppDatabase {
        return Database().getDatabaseBuilder().build()
    }
}