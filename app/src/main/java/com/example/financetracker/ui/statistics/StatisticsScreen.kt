package com.example.financetracker.ui.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.util.formatCurrency

@Composable
fun StatisticsScreen(
    totalIncome: Double,
    totalExpenses: Double,
    expensesByCategory: List<CategoryExpense>,
    selectedPeriod: StatisticsPeriod,
    onPeriodSelected: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val chartColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Статистика",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    text = "Период",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

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
            }

            item {
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

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = formatCurrency(totalIncome),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
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

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = formatCurrency(totalExpenses),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (expensesByCategory.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Структура расходов",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            ExpensePieChart(
                                expenses = expensesByCategory,
                                colors = chartColors,
                                totalExpenses = totalExpenses
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Расходы по категориям",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (expensesByCategory.isEmpty()) {
                item {
                    Text(
                        text = "Пока нет расходов",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                items(
                    count = expensesByCategory.size
                ) { index ->
                    val categoryExpense = expensesByCategory[index]
                    val categoryColor =
                        chartColors[index % chartColors.size]

                    Column {
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
                                text = formatCurrency(categoryExpense.amount),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        LinearProgressIndicator(
                            progress = {
                                categoryExpense.percentage.toFloat()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Canvas(
                                modifier = Modifier.size(10.dp)
                            ) {
                                drawCircle(
                                    color = categoryColor
                                )
                            }

                            Spacer(
                                modifier = Modifier.size(6.dp)
                            )

                            Text(
                                text = "${(categoryExpense.percentage * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ExpensePieChart(
    expenses: List<CategoryExpense>,
    colors: List<Color>,
    totalExpenses: Double
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(220.dp)
        ) {
            val diameter = size.minDimension
            val strokeWidth = 55.dp.toPx()

            var startAngle = -90f

            expenses.forEachIndexed { index, expense ->

                val sweepAngle =
                    (expense.percentage * 360).toFloat()

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = strokeWidth
                    ),
                    topLeft = Offset(
                        x = (size.width - diameter) / 2,
                        y = (size.height - diameter) / 2
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        diameter,
                        diameter
                    )
                )

                startAngle += sweepAngle
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatCurrency(totalExpenses),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "расходы",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
