package com.example.financetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.data.TransactionRepository
import com.example.financetracker.data.local.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CategoryExpense(
    val category: String,
    val amount: Double,
    val percentage: Double
)

enum class StatisticsPeriod {
    TODAY,
    THIS_MONTH,
    ALL_TIME
}

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

    private val selectedPeriod =
        MutableStateFlow(StatisticsPeriod.ALL_TIME)

    val period: StateFlow<StatisticsPeriod> = selectedPeriod

    fun setPeriod(period: StatisticsPeriod) {
        selectedPeriod.value = period
    }

    private val filteredTransactions: StateFlow<List<TransactionEntity>> =
        combine(transactions, selectedPeriod) { list, period ->
            when (period) {
                StatisticsPeriod.ALL_TIME -> {
                    list
                }

                StatisticsPeriod.TODAY -> {
                    val calendar = java.util.Calendar.getInstance()

                    calendar.set(
                        java.util.Calendar.HOUR_OF_DAY,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.MINUTE,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.SECOND,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.MILLISECOND,
                        0
                    )

                    val startOfDay = calendar.timeInMillis

                    list.filter { it.date >= startOfDay }
                }

                StatisticsPeriod.THIS_MONTH -> {
                    val calendar = java.util.Calendar.getInstance()

                    calendar.set(
                        java.util.Calendar.DAY_OF_MONTH,
                        1
                    )
                    calendar.set(
                        java.util.Calendar.HOUR_OF_DAY,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.MINUTE,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.SECOND,
                        0
                    )
                    calendar.set(
                        java.util.Calendar.MILLISECOND,
                        0
                    )

                    val startOfMonth = calendar.timeInMillis

                    list.filter { it.date >= startOfMonth }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val totalIncome: StateFlow<Double> =
        filteredTransactions
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
        filteredTransactions
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
        filteredTransactions
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