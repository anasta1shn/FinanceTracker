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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
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

        // Заголовок
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад"
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column {

                Text(
                    text = if (transaction == null) {
                        "Новая операция"
                    } else {
                        "Редактирование"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (transaction == null) {
                        "Добавьте новую финансовую операцию"
                    } else {
                        "Измените данные операции"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Тип операции
        Text(
            text = "Тип операции",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
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
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Расход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = false
                        category = ""
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

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
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Доход")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        isIncome = true
                        category = ""
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Доход")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Сумма
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Сумма")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = null
                )
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Категория
        Text(
            text = "Категория",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactionCategories) { item ->

                val isSelected = category == item

                Card(
                    onClick = {
                        category = item
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CategoryIcon(
                            category = item,
                            isIncome = isIncome
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Дата
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

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

        // Описание
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Описание")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null
                )
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Сохранение
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
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = if (transaction == null) {
                    "Сохранить"
                } else {
                    "Сохранить изменения"
                }
            )
        }
    }
}

@Composable
private fun CategoryIcon(
    category: String,
    isIncome: Boolean
) {
    val icon = when (category) {
        "Еда" -> Icons.Default.Restaurant
        "Транспорт" -> Icons.Default.DirectionsCar
        "Покупки" -> Icons.Default.ShoppingBag
        "Развлечения" -> Icons.Default.Movie
        "Здоровье" -> Icons.Default.Favorite
        "Жильё" -> Icons.Default.Home
        "Образование" -> Icons.Default.School
        "Зарплата" -> Icons.Default.Work
        "Подработка" -> Icons.Default.WorkOutline
        "Подарок" -> Icons.Default.CardGiftcard
        "Инвестиции" -> Icons.Default.TrendingUp
        else -> Icons.Default.MoreHoriz
    }

    Icon(
        imageVector = icon,
        contentDescription = category,
        tint = if (isIncome) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        },
        modifier = Modifier.size(28.dp)
    )
}