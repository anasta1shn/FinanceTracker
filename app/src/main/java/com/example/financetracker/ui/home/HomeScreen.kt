package com.example.financetracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.local.TransactionEntity
import com.example.financetracker.util.formatCurrency

@Composable
fun HomeScreen(
    balance: Double,
    income: Double,
    expenses: Double,
    currency: String,
    recentTransactions: List<TransactionEntity>,
    onAddTransaction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction
            ) {
                Text(
                    text = "+"
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Сентябрь 2026",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Добрый день 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            item {

                BalanceCard(
                    balance = formatCurrency(
                        amount = balance,
                        currency = currency
                    )
                )
            }

            item {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SummaryCard(
                        title = "Доходы",
                        amount = "+ ${
                            formatCurrency(
                                amount = income,
                                currency = currency
                            )
                        }",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    SummaryCard(
                        title = "Расходы",
                        amount = "- ${
                            formatCurrency(
                                amount = expenses,
                                currency = currency
                            )
                        }",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Последние операции",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (recentTransactions.isEmpty()) {

                item {

                    Text(
                        text = "Пока нет операций",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

            } else {

                items(
                    items = recentTransactions,
                    key = { it.id }
                ) { transaction ->

                    TransactionItem(
                        transaction = transaction,
                        currency = currency
                    )
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(80.dp)
                )
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Баланс",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = balance,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: TransactionEntity,
    currency: String
) {

    val sign = if (transaction.isIncome) "+" else "-"

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

                if (transaction.description.isNotBlank()) {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

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
        }
    }
}
