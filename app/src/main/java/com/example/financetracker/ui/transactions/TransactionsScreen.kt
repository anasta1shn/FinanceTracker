package com.example.financetracker.ui.transactions

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    transactions: List<TransactionEntity>,
    onDelete: (TransactionEntity) -> Unit,
    onEdit: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredTransactions = transactions.filter { transaction ->
        transaction.description.contains(
            searchQuery,
            ignoreCase = true
        ) || transaction.category.contains(
            searchQuery,
            ignoreCase = true
        )
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

        Spacer(
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Поиск")
            },
            placeholder = {
                Text("Описание или категория")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.padding(top = 16.dp)
        )

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
                        "Попробуйте изменить запрос"
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
                        onDelete = {
                            onDelete(transaction)
                        },
                        onEdit = {
                            onEdit(transaction)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionCard(
    transaction: TransactionEntity,
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

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                text = "$sign ${transaction.amount} ₽",
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