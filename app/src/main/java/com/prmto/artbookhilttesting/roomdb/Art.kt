package com.prmto.artbookhilttesting.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val artistName: String,
    val year: Int,
    val imageUrl: String
)
