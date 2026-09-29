package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.financetracker.ui.home.HomeScreen
import com.example.financetracker.ui.add.AddTransactionScreen
import com.example.financetracker.data.TransactionRepository
import com.example.financetracker.data.local.DatabaseProvider
import com.example.financetracker.ui.add.AddTransactionViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financetracker.ui.add.AddTransactionViewModelFactory
import com.example.financetracker.ui.transactions.TransactionsScreen
import com.example.financetracker.ui.transactions.TransactionsViewModel
import com.example.financetracker.ui.transactions.TransactionsViewModelFactory
import androidx.compose.ui.unit.dp
import com.example.financetracker.ui.home.HomeViewModel
import com.example.financetracker.ui.home.HomeViewModelFactory
import androidx.compose.runtime.collectAsState
import com.example.financetracker.data.local.TransactionEntity
import com.example.financetracker.ui.statistics.StatisticsScreen
import com.example.financetracker.ui.statistics.StatisticsViewModel
import com.example.financetracker.ui.statistics.StatisticsViewModelFactory
import com.example.financetracker.ui.theme.FinanceTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FinanceTrackerTheme {
                FinanceTrackerApp()
            }
        }
    }
}

@Composable
fun FinanceTrackerApp() {

    val context = LocalContext.current

    val database = remember {
        DatabaseProvider.getDatabase(context)
    }

    val repository = remember {
        TransactionRepository(database.transactionDao())
    }

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val homeTransactions by homeViewModel.transactions.collectAsState()
    val balance by homeViewModel.balance.collectAsState()
    val income by homeViewModel.income.collectAsState()
    val expenses by homeViewModel.expenses.collectAsState()

    val addTransactionViewModel: AddTransactionViewModel = viewModel(
        factory = AddTransactionViewModelFactory(repository)
    )

    val transactionsViewModel: TransactionsViewModel = viewModel(
        factory = TransactionsViewModelFactory(repository)
    )

    val statisticsViewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModelFactory(repository)
    )

    val totalIncome by statisticsViewModel.totalIncome.collectAsState()
    val totalExpenses by statisticsViewModel.totalExpenses.collectAsState()
    val expensesByCategory by statisticsViewModel.expensesByCategory.collectAsState()

    val transactions by transactionsViewModel.transactions.collectAsState()

    var selectedScreen by remember {
        mutableIntStateOf(0)
    }

    var showAddTransactionScreen by remember {
        mutableStateOf(false)
    }

    var editingTransaction by remember {
        mutableStateOf<TransactionEntity?>(null)
    }

    val screens = listOf(
        "Главная",
        "Операции",
        "Статистика",
        "Настройки"
    )

    // Если открыт экран добавления операции,
    // показываем его вместо основного интерфейса
    if (showAddTransactionScreen || editingTransaction != null) {

        AddTransactionScreen(
            transaction = editingTransaction,
            onBack = {
                showAddTransactionScreen = false
                editingTransaction = null
            },
            onSave = { amount, isIncome, category, description, date ->

                if (editingTransaction == null) {
                    addTransactionViewModel.addTransaction(
                        amount = amount,
                        isIncome = isIncome,
                        category = category,
                        description = description,
                        date = date
                    )
                } else {
                    val updatedTransaction = editingTransaction!!.copy(
                        amount = amount,
                        isIncome = isIncome,
                        category = category,
                        description = description,
                        date = date
                    )

                    transactionsViewModel.updateTransaction(
                        updatedTransaction
                    )
                }

                showAddTransactionScreen = false
                editingTransaction = null
            }
        )

    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                NavigationBar {

                    screens.forEachIndexed { index, title ->

                        NavigationBarItem(
                            selected = selectedScreen == index,

                            onClick = {
                                selectedScreen = index
                            },

                            icon = {
                                Text(
                                    text = when (index) {
                                        0 -> "⌂"
                                        1 -> "₽"
                                        2 -> "▥"
                                        else -> "⚙"
                                    }
                                )
                            },

                            label = {
                                Text(text = title)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->

            when (selectedScreen) {

                0 -> HomeScreen(
                    balance = balance,
                    income = income,
                    expenses = expenses,
                    recentTransactions = homeTransactions.take(5),
                    onAddTransaction = {
                        editingTransaction = null
                        showAddTransactionScreen = true
                    },
                    modifier = Modifier.padding(innerPadding)
                )

                1 -> TransactionsScreen(
                    transactions = transactions,
                    onDelete = { transaction ->
                        transactionsViewModel.deleteTransaction(transaction)
                    },
                    onEdit = { transaction ->
                        editingTransaction = transaction
                    },
                    modifier = Modifier.padding(innerPadding)
                )

                2 -> StatisticsScreen(
                    totalIncome = totalIncome,
                    totalExpenses = totalExpenses,
                    expensesByCategory = expensesByCategory,
                    modifier = Modifier.padding(innerPadding)
                )

                3 -> SettingsScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    SimpleScreen(
        title = "Настройки",
        modifier = modifier
    )
}

@Composable
fun SimpleScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title
        )
    }
}