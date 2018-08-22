package com.enpassio.practicewithroom

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.enpassio.practicewithroom.database.AppDatabase
import com.enpassio.practicewithroom.database.TaskEntry


class AddTaskViewModel(database: AppDatabase, taskId: Int) : ViewModel() {

    val task: LiveData<TaskEntry>

    init {
        task = database.taskDao().loadTaskById(taskId)
    }
}