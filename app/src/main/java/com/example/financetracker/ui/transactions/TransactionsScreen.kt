package com.example.financetracker.ui.transactions

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.local.TransactionEntity
import com.example.financetracker.util.formatCurrency
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    transactions: List<TransactionEntity>,
    currency: String,
    onDelete: (TransactionEntity) -> Unit,
    onEdit: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }
    var selectedFilter by remember { mutableStateOf("Все") }
    var selectedDateFilter by remember { mutableStateOf("Всё время") }

    val filters = listOf("Все", "Доходы", "Расходы")
    val dateFilters = listOf(
        "Сегодня",
        "Эта неделя",
        "Этот месяц",
        "Всё время"
    )

    val dateFilterScrollState = rememberScrollState()

    val now = System.currentTimeMillis()

    val dateFilteredTransactions = transactions.filter { transaction ->
        when (selectedDateFilter) {
            "Сегодня" -> {
                val calendar = Calendar.getInstance()

                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                transaction.date >= calendar.timeInMillis
            }

            "Эта неделя" -> {
                val calendar = Calendar.getInstance()

                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                transaction.date >= calendar.timeInMillis &&
                        transaction.date <= now
            }

            "Этот месяц" -> {
                val calendar = Calendar.getInstance()

                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                transaction.date >= calendar.timeInMillis
            }

            else -> true
        }
    }

    val filteredTransactions = dateFilteredTransactions.filter { transaction ->
        val matchesSearch =
            transaction.description.contains(
                searchQuery,
                ignoreCase = true
            ) ||
                    transaction.category.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        val matchesFilter = when (selectedFilter) {
            "Доходы" -> transaction.isIncome
            "Расходы" -> !transaction.isIncome
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Операции",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Поиск") },
            placeholder = { Text("Описание или категория") },
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(top = 12.dp))

        // Фильтр по типу операции
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            filters.forEach { filter ->
                if (selectedFilter == filter) {
                    Button(
                        onClick = {
                            selectedFilter = filter
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(filter)
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            selectedFilter = filter
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(filter)
                    }
                }

                if (filter != filters.last()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        // Фильтр по дате с горизонтальной прокруткой
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(dateFilterScrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dateFilters.forEach { filter ->
                if (selectedDateFilter == filter) {
                    Button(
                        onClick = {
                            selectedDateFilter = filter
                        }
                    ) {
                        Text(filter)
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            selectedDateFilter = filter
                        }
                    ) {
                        Text(filter)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if (filteredTransactions.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (transactions.isEmpty()) {
                        "Пока нет операций"
                    } else {
                        "Ничего не найдено"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (transactions.isEmpty()) {
                        "Добавьте первую операцию"
                    } else {
                        "Попробуйте изменить фильтры"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredTransactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        currency = currency,
                        onDelete = {
                            transactionToDelete = transaction
                        },
                        onEdit = {
                            onEdit(transaction)
                        }
                    )
                }
            }
        }
    }

    // Диалог подтверждения удаления
    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                transactionToDelete = null
            },
            title = {
                Text("Удалить операцию?")
            },
            text = {
                Text("Вы действительно хотите удалить эту операцию?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        transactionToDelete?.let { transaction ->
                            onDelete(transaction)
                        }

                        transactionToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        transactionToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun TransactionCard(
    transaction: TransactionEntity,
    currency: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val sign = if (transaction.isIncome) "+" else "-"

    val dateFormatter = remember {
        SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = dateFormatter.format(
                        Date(transaction.date)
                    ),
                    style = MaterialTheme.typography.bodySmall
                )

                if (transaction.description.isNotBlank()) {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "$sign ${
                    formatCurrency(
                        amount = transaction.amount,
                        currency = currency
                    )
                }",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onEdit
            ) {
                Text("Изменить")
            }

            TextButton(
                onClick = onDelete
            ) {
                Text("Удалить")
            }
        }
    }
}
