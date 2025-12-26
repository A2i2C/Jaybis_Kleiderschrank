package com.example.kleiderschrank.datahandling.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kleidung")
data class Kleidung(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String? = null,

    val bildPfad: String
)

