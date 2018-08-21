package com.enpassio.practicewithroom.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import android.util.Log


@Database(entities = arrayOf(TaskEntry::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        private val LOG_TAG = AppDatabase::class.java.simpleName
        private val LOCK = Any()
        private val DATABASE_NAME = "todolist"
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, AppDatabase.DATABASE_NAME)
                            // call allowMainThreadQueries before building the instance
                            // Queries should be done in a separate thread to avoid locking the UI
                            // We will allow this ONLY TEMPORALLY to see that our DB is working
                            .allowMainThreadQueries()
                            .build()
                }
            }
            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance!!
        }
    }

}