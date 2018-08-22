package com.enpassio.practicewithroom

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.enpassio.practicewithroom.database.AppDatabase


class AddTaskViewModelFactory(private val mDb: AppDatabase,
                              private val mTaskId: Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AddTaskViewModel(mDb, mTaskId) as T
    }
}