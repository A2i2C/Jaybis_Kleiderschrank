package com.example.kleiderschrank.datahandling.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kleiderschrank.datahandling.entity.Kleidung

@Dao
interface KleidungDao {
    @Insert
     fun insert(kleidung: Kleidung)
    @Query("SELECT * FROM kleidung")
     fun getAll(): List<Kleidung>
}

