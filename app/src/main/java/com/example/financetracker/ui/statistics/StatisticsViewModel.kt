package com.example.financetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.TransactionRepository
import com.example.financetracker.data.local.TransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CategoryExpense(
    val category: String,
    val amount: Double,
    val percentage: Double
)

class StatisticsViewModel(
    repository: TransactionRepository
) : ViewModel() {

    private val transactions: StateFlow<List<TransactionEntity>> =
        repository.observeTransactions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val totalIncome: StateFlow<Double> =
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

    val totalExpenses: StateFlow<Double> =
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

    val expensesByCategory: StateFlow<List<CategoryExpense>> =
        transactions
            .map { list ->

                val expenses = list.filter { !it.isIncome }

                val totalExpenses = expenses.sumOf { it.amount }

                expenses
                    .groupBy { it.category }
                    .map { (category, transactions) ->

                        val amount = transactions.sumOf { it.amount }

                        val percentage =
                            if (totalExpenses > 0) {
                                amount / totalExpenses
                            } else {
                                0.0
                            }

                        CategoryExpense(
                            category = category,
                            amount = amount,
                            percentage = percentage
                        )
                    }
                    .sortedByDescending { it.amount }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}