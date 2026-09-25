package com.example.financetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.TransactionRepository
import com.example.financetracker.data.local.TransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    repository: TransactionRepository
) : ViewModel() {

    val transactions: StateFlow<List<TransactionEntity>> =
        repository.observeTransactions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val balance: StateFlow<Double> =
        transactions
            .map { list ->
                list.sumOf { transaction ->
                    if (transaction.isIncome) {
                        transaction.amount
                    } else {
                        -transaction.amount
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0.0
            )

    val income: StateFlow<Double> =
        transactions
            .map { list ->
                list
                    .filter { it.isIncome }
                    .sumOf { it.amount }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0.0
            )

    val expenses: StateFlow<Double> =
        transactions
            .map { list ->
                list
                    .filter { !it.isIncome }
                    .sumOf { it.amount }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0.0
            )
}