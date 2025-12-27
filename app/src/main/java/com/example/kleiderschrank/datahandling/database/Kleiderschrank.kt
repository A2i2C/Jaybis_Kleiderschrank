package com.example.kleiderschrank.datahandling.database

import com.example.kleiderschrank.datahandling.dao.KleidungDao
import com.example.kleiderschrank.datahandling.entity.Kleidung
import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Kleidung::class],
    version = 1,
    exportSchema = false
)

abstract class Kleiderschrank: RoomDatabase() {

    abstract fun kleidungDao(): KleidungDao

    companion object {
        @Volatile
        private var INSTANCE: Kleiderschrank? = null

        fun getDatabase(context: Context): Kleiderschrank {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Kleiderschrank::class.java,
                    "kleiderschrank.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}