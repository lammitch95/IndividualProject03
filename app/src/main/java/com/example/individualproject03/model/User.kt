package com.example.individualproject03.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,

    var easyModeProgress: Int = 0,
    var hardModeProgress: Int = 0
)