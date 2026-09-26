package com.example.financetracker.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.TransactionRepository
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    fun addTransaction(
        amount: Double,
        isIncome: Boolean,
        category: String,
        description: String,
        date: Long
    ) {
        viewModelScope.launch {
            repository.addTransaction(
                amount = amount,
                isIncome = isIncome,
                category = category,
                description = description,
                date = date
            )
        }
    }
}