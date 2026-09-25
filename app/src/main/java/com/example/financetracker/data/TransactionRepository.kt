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
        description: String
    ) {
        dao.insert(
            TransactionEntity(
                amount = amount,
                isIncome = isIncome,
                category = category,
                description = description
            )
        )
    }

    fun observeTransactions(): Flow<List<TransactionEntity>> {
        return dao.observeAll()
    }

    suspend fun deleteTransaction(
        transaction: TransactionEntity
    ) {
        dao.delete(transaction)
    }
}