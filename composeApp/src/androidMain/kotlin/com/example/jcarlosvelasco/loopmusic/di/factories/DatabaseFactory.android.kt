package com.example.jcarlosvelasco.loopmusic.di.factories

import android.content.Context
import androidx.room.Room
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.AppDatabase
import com.example.jcarlosvelasco.loopmusic.infrastructure.database.getRoomDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class DatabaseFactory actual constructor(): KoinComponent {


    actual fun createDatabase(): AppDatabase {
        val context: Context by inject()
        val builder = Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
        return getRoomDatabase(builder)
    }
}