package com.kagan.chatapp.db.dao

import androidx.room.*
import com.kagan.chatapp.db.entities.UsersEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(user: UsersEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): List<UsersEntity>

    @Query("DELETE FROM users WHERE Id= :id")
    suspend fun deleteUsers(id: String)
}