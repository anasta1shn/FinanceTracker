package com.example.financetracker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
    isDarkTheme: Boolean,
    onCurrencySelected: (String) -> Unit,
    onBudgetSaved: (Double?) -> Unit,
    onThemeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyMenuExpanded by remember {
        mutableStateOf(false)
    }

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

        Text(
            text = "Настройте приложение под себя",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
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
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Выберите валюту, которая будет использоваться в приложении.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Валюта",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Текущая: $currency",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        TextButton(
                            onClick = {
                                currencyMenuExpanded = true
                            }
                        ) {
                            Text(
                                text = currency,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
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
                                        Text(
                                            text = item
                                        )
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
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Установите максимальную сумму расходов на месяц.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { value ->
                        budgetText = value
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Месячный бюджет")
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
                    Text("Сохранить бюджет")
                }

                if (monthlyBudget != null) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Текущий бюджет",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = formatCurrency(
                            amount = monthlyBudget,
                            currency = currency
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Внешний вид",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = if (isDarkTheme) {
                            "Сейчас используется тёмная тема"
                        } else {
                            "Сейчас используется светлая тема"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Изменить оформление приложения",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { enabled ->
                        onThemeChanged(enabled)
                    }
                )
            }
        }
    }
}

