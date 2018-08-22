package com.enpassio.practicewithroom

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import com.enpassio.practicewithroom.database.AppDatabase
import com.enpassio.practicewithroom.database.TaskEntry


class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Add a tasks member variable for a list of TaskEntry objects wrapped in a LiveData
    val tasks: LiveData<List<TaskEntry>>

    init {
        // In the constructor use the loadAllTasks of the taskDao to initialize the tasks variable
        val database = AppDatabase.getInstance(this.getApplication())
        Log.d(TAG, "Actively retrieving the tasks from the DataBase")
        tasks = database.taskDao().loadAllTasks()
    }

    companion object {
        // Constant for logging
        private val TAG = MainViewModel::class.java.simpleName
    }
}