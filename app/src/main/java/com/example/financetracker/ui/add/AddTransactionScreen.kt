package com.example.financetracker.ui.add

import android.app.DatePickerDialog
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.local.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val expenseCategories = listOf(
    "Еда",
    "Транспорт",
    "Покупки",
    "Развлечения",
    "Здоровье",
    "Жильё",
    "Образование",
    "Другое"
)

private val incomeCategories = listOf(
    "Зарплата",
    "Подработка",
    "Подарок",
    "Инвестиции",
    "Другое"
)

@Composable
fun AddTransactionScreen(
    transaction: TransactionEntity? = null,
    onBack: () -> Unit,
    onSave: (
        amount: Double,
        isIncome: Boolean,
        category: String,
        description: String,
        date: Long
    ) -> Unit
) {
    val context = LocalContext.current

    var amount by remember(transaction) {
        mutableStateOf(
            transaction?.amount?.toString() ?: ""
        )
    }

    var isIncome by remember(transaction) {
        mutableStateOf(
            transaction?.isIncome ?: false
        )
    }

    var category by remember(transaction) {
        mutableStateOf(
            transaction?.category ?: ""
        )
    }

    var description by remember(transaction) {
        mutableStateOf(
            transaction?.description ?: ""
        )
    }

    var selectedDate by remember(transaction) {
        mutableLongStateOf(
            transaction?.date ?: System.currentTimeMillis()
        )
    }

    var categoryMenuExpanded by remember {
        mutableStateOf(false)
    }

    val transactionCategories = if (isIncome) {
        incomeCategories
    } else {
        expenseCategories
    }

    val dateFormatter = remember {
        SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onBack
            ) {
                Text("Назад")
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = if (transaction == null) {
                    "Новая операция"
                } else {
                    "Редактирование"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Тип операции"
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!isIncome) {
                Button(
                    onClick = {
                        isIncome = false

                        if (category !in expenseCategories) {
                            category = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Расход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = false
                        category = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Расход")
                }
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            if (isIncome) {
                Button(
                    onClick = {
                        isIncome = true

                        if (category !in incomeCategories) {
                            category = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Доход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = true
                        category = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Доход")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Сумма")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    categoryMenuExpanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (category.isBlank()) {
                        "Выберите категорию"
                    } else {
                        category
                    }
                )
            }

            DropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = {
                    categoryMenuExpanded = false
                }
            ) {
                transactionCategories.forEach { item ->

                    DropdownMenuItem(
                        text = {
                            Text(item)
                        },
                        onClick = {
                            category = item
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                }

                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->

                        val newCalendar = Calendar.getInstance().apply {
                            set(
                                year,
                                month,
                                dayOfMonth,
                                12,
                                0,
                                0
                            )
                            set(
                                Calendar.MILLISECOND,
                                0
                            )
                        }

                        selectedDate = newCalendar.timeInMillis
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Дата: ${
                    dateFormatter.format(
                        Date(selectedDate)
                    )
                }"
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Описание")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                val amountValue = amount
                    .replace(",", ".")
                    .toDoubleOrNull()

                if (
                    amountValue != null &&
                    amountValue > 0 &&
                    category.isNotBlank()
                ) {
                    onSave(
                        amountValue,
                        isIncome,
                        category,
                        description,
                        selectedDate
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (transaction == null) {
                    "Сохранить"
                } else {
                    "Сохранить изменения"
                }
            )
        }
    }
}

