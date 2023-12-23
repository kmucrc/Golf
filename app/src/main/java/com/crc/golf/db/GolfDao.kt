package com.crc.golf.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GolfDao {
    @Query("SELECT * FROM GolfEntity ORDER BY createddate DESC")
    fun getAll() : List<GolfEntity>

    @Insert
    fun insertTodo(todo : GolfEntity)

    @Delete
    fun deleteTodo(todo : GolfEntity)

    @Query("UPDATE GolfEntity SET download = :download WHERE id = :id")
    fun updateDownloadTodo(id: Int, download: Boolean)
}