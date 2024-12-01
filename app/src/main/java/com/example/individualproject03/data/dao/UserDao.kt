package com.example.individualproject03.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.individualproject03.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun checkIfEmailExists(email: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET easyModeProgress = :newProgress WHERE email = :email")
    suspend fun updateEasyModeProgress(email: String, newProgress: Int)

    @Query("UPDATE users SET hardModeProgress = :newProgress WHERE email = :email")
    suspend fun updateHardModeProgress(email: String, newProgress: Int)
}