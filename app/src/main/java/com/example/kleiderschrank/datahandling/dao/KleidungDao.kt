package com.example.kleiderschrank.datahandling.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kleiderschrank.datahandling.entity.Kleidung

class KleidungDao {

    @Dao
    interface KleidungDao {

        @Insert
        suspend fun insert(kleidung: Kleidung)

        @Query("SELECT * FROM kleidung")
        suspend fun getAll(): List<Kleidung>
    }

}