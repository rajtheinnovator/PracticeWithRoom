package com.enpassio.practicewithroom.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*


@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY priority")
    fun loadAllTasks(): LiveData<List<TaskEntry>>

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

    @Query("SELECT * FROM task WHERE id = :id")
    fun loadTaskById(id: Int): LiveData<TaskEntry>
}