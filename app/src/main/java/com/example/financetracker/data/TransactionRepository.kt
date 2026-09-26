package com.example.financetracker.data

import com.example.financetracker.data.local.TransactionDao
import com.example.financetracker.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val dao: TransactionDao
) {

    suspend fun addTransaction(
        amount: Double,
        isIncome: Boolean,
        category: String,
        description: String,
        date: Long
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            isIncome = isIncome,
            category = category,
            description = description,
            date = date
        )

        dao.insert(transaction)
    }

    fun observeTransactions(): Flow<List<TransactionEntity>> {
        return dao.observeAll()
    }

    suspend fun deleteTransaction(
        transaction: TransactionEntity
    ) {
        dao.delete(transaction)
    }

    suspend fun updateTransaction(
        transaction: TransactionEntity
    ) {
        dao.update(transaction)
    }
}