package com.example.financetracker.ui.settings

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.financetracker.util.formatCurrency

@Composable
fun SettingsScreen(
    currency: String,
    monthlyBudget: Double?,
    onCurrencySelected: (String) -> Unit,
    onBudgetSaved: (Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyMenuExpanded by remember { mutableStateOf(false) }

    var budgetText by remember(monthlyBudget) {
        mutableStateOf(
            monthlyBudget?.let {
                if (it % 1.0 == 0.0) {
                    it.toInt().toString()
                } else {
                    it.toString()
                }
            } ?: ""
        )
    }

    val currencies = listOf("₽", "$", "€")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Основные настройки",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Валюта",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Column {
                        TextButton(
                            onClick = {
                                currencyMenuExpanded = true
                            }
                        ) {
                            Text(
                                text = currency,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        DropdownMenu(
                            expanded = currencyMenuExpanded,
                            onDismissRequest = {
                                currencyMenuExpanded = false
                            }
                        ) {
                            currencies.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = item)
                                    },
                                    onClick = {
                                        onCurrencySelected(item)
                                        currencyMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Бюджет",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Месячный бюджет",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { value ->
                        budgetText = value
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Сумма")
                    },
                    placeholder = {
                        Text("Например, 50000")
                    },
                    suffix = {
                        Text(currency)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {
                        val budget = budgetText
                            .replace(',', '.')
                            .toDoubleOrNull()

                        if (budget != null && budget > 0) {
                            onBudgetSaved(budget)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }

                if (monthlyBudget != null) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Текущий бюджет: ${
                            formatCurrency(
                                amount = monthlyBudget,
                                currency = currency
                            )
                        }",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            budgetText = ""
                            onBudgetSaved(null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сбросить бюджет")
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Внешний вид",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Светлая тема",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
