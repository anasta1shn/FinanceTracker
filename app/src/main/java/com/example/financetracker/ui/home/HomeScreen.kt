package com.example.financetracker.ui.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    monthlyBudget: Double?,
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
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = "Добавить операцию"
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
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

            // Баланс
            item {

                BalanceCard(
                    balance = formatCurrency(
                        amount = balance,
                        currency = currency
                    )
                )
            }

            // Бюджет
            if (monthlyBudget != null) {

                item {

                    BudgetCard(
                        spent = expenses,
                        budget = monthlyBudget,
                        currency = currency
                    )
                }
            }

            // Доходы и расходы
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    SummaryCard(
                        title = "Доходы",
                        amount = formatCurrency(
                            amount = income,
                            currency = currency
                        ),
                        amountColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Default.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )

                    SummaryCard(
                        title = "Расходы",
                        amount = formatCurrency(
                            amount = expenses,
                            currency = currency
                        ),
                        amountColor = MaterialTheme.colorScheme.error,
                        icon = Icons.Default.Payments,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Последние операции
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text = "Пока нет операций",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Добавьте первую операцию",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Баланс",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = balance,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Текущее состояние финансов",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun BudgetCard(
    spent: Double,
    budget: Double,
    currency: String
) {
    val progress = if (budget > 0) {
        (spent / budget)
            .coerceIn(0.0, 1.0)
            .toFloat()
    } else {
        0f
    }

    val remaining = budget - spent

    val spentPercentage = if (budget > 0) {
        spent / budget
    } else {
        0.0
    }

    val statusText = when {
        spentPercentage > 1.0 -> {
            "Бюджет превышен"
        }

        spentPercentage >= 0.8 -> {
            "Бюджет почти исчерпан"
        }

        else -> {
            "Всё под контролем"
        }
    }

    val statusColor = when {
        spentPercentage > 1.0 -> {
            MaterialTheme.colorScheme.error
        }

        spentPercentage >= 0.8 -> {
            MaterialTheme.colorScheme.tertiary
        }

        else -> {
            MaterialTheme.colorScheme.primary
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Бюджет на месяц",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = formatCurrency(
                        amount = spent,
                        currency = currency
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = " из ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formatCurrency(
                        amount = budget,
                        currency = currency
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            LinearProgressIndicator(
                progress = {
                    progress
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = when {
                    remaining >= 0 -> {
                        "Осталось: ${
                            formatCurrency(
                                amount = remaining,
                                currency = currency
                            )
                        }"
                    }

                    else -> {
                        "Превышение: ${
                            formatCurrency(
                                amount = -remaining,
                                currency = currency
                            )
                        }"
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    amountColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
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
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = amountColor
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
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

    val amountColor = if (transaction.isIncome) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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

            CategoryIcon(
                category = transaction.category,
                isIncome = transaction.isIncome
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (transaction.description.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "$sign ${
                    formatCurrency(
                        amount = transaction.amount,
                        currency = currency
                    )
                }",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
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
        else -> {
            if (isIncome) {
                Icons.Default.AccountBalance
            } else {
                Icons.Default.MoreHoriz
            }
        }
    }

    Icon(
        imageVector = icon,
        contentDescription = category,
        modifier = Modifier.size(28.dp),
        tint = if (isIncome) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
    )
}

