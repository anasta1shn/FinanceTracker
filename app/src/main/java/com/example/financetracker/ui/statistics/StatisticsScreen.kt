package com.example.financetracker.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsScreen(
    totalIncome: Double,
    totalExpenses: Double,
    expensesByCategory: List<CategoryExpense>,
    selectedPeriod: StatisticsPeriod,
    onPeriodSelected: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Статистика",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Период",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (selectedPeriod == StatisticsPeriod.TODAY) {
                Button(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.TODAY)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сегодня")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.TODAY)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сегодня")
                }
            }

            if (selectedPeriod == StatisticsPeriod.THIS_MONTH) {
                Button(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.THIS_MONTH)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Месяц")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.THIS_MONTH)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Месяц")
                }
            }

            if (selectedPeriod == StatisticsPeriod.ALL_TIME) {
                Button(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.ALL_TIME)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Всё время")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        onPeriodSelected(StatisticsPeriod.ALL_TIME)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Всё время")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Доходы",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${totalIncome} ₽",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Расходы",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${totalExpenses} ₽",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Расходы по категориям",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (expensesByCategory.isEmpty()) {
            Text(
                text = "Пока нет расходов",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            expensesByCategory.forEach { categoryExpense ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = categoryExpense.category,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${categoryExpense.amount} ₽",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = {
                        categoryExpense.percentage.toFloat()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${(categoryExpense.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}