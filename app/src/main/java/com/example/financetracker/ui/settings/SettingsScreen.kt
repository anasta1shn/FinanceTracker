package com.example.financetracker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
            monthlyBudget?.toString() ?: ""
        )
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Заголовок
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {
                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Настройте приложение под себя",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Валюта
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Column {
                        Text(
                            text = "Валюта",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Используется для отображения сумм",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {
                        currencyMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Текущая валюта: $currency"
                    )
                }

                DropdownMenu(
                    expanded = currencyMenuExpanded,
                    onDismissRequest = {
                        currencyMenuExpanded = false
                    }
                ) {
                    listOf("₽", "$", "€").forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when (item) {
                                        "₽" -> "₽  Рубль"
                                        "$" -> "$  Доллар"
                                        else -> "€  Евро"
                                    }
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

        // Бюджет
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Column {
                        Text(
                            text = "Месячный бюджет",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Лимит расходов на текущий месяц",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = budgetText,
                    onValueChange = {
                        budgetText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Бюджет")
                    },
                    placeholder = {
                        Text("Например, 50000")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Wallet,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val value = budgetText
                                .replace(",", ".")
                                .toDoubleOrNull()

                            if (value != null && value > 0) {
                                onBudgetSaved(value)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Text("Сохранить")
                    }

                    Button(
                        onClick = {
                            budgetText = ""
                            onBudgetSaved(null)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Сбросить")
                    }
                }
            }
        }

        // Тема
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Тёмная тема",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = if (isDarkTheme) {
                            "Тёмная тема включена"
                        } else {
                            "Используется светлая тема"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChanged
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

