package com.example.financetracker.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    onSave: (
        amount: Double,
        isIncome: Boolean,
        category: String,
        description: String
    ) -> Unit
) {

    var amount by remember {
        mutableStateOf("")
    }

    var isIncome by remember {
        mutableStateOf(false)
    }

    var category by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
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
                text = "Новая операция"
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
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Расход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Расход")
                }
            }

            Spacer(
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            if (isIncome) {
                Button(
                    onClick = {
                        isIncome = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Доход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = true
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

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Категория")
            },
            singleLine = true
        )

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

                if (amountValue != null && amountValue > 0) {

                    onSave(
                        amountValue,
                        isIncome,
                        category,
                        description
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}