package com.example.financetracker.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var database: FinanceDatabase? = null

    fun getDatabase(context: Context): FinanceDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                FinanceDatabase::class.java,
                "finance_tracker.db"
            ).build().also {
                database = it
            }
        }
    }
}