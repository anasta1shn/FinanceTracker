package com.example.financetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Double,

    val isIncome: Boolean,

    val category: String,

    val description: String,

    val date: Long = System.currentTimeMillis()
)