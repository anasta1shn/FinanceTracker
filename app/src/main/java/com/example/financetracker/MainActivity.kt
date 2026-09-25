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
import androidx.compose.ui.unit.dp
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

    var selectedScreen by remember {
        mutableIntStateOf(0)
    }

    var showAddTransactionScreen by remember {
        mutableStateOf(false)
    }

    val screens = listOf(
        "Главная",
        "Операции",
        "Статистика",
        "Настройки"
    )

    // Если открыт экран добавления операции,
    // показываем его вместо основного интерфейса
    if (showAddTransactionScreen) {

        AddTransactionScreen(
            onBack = {
                showAddTransactionScreen = false
            },
            onSave = { amount, isIncome, category, description ->

                // Пока данные никуда не сохраняем.
                // Room добавим на следующем этапе.

                showAddTransactionScreen = false
            }
        )

        return
    }

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
                modifier = Modifier.padding(innerPadding),
                onAddTransaction = {
                    showAddTransactionScreen = true
                }
            )

            1 -> TransactionsScreen(
                modifier = Modifier.padding(innerPadding)
            )

            2 -> StatisticsScreen(
                modifier = Modifier.padding(innerPadding)
            )

            3 -> SettingsScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}



@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier
) {
    SimpleScreen(
        title = "Операции",
        modifier = modifier
    )
}

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier
) {
    SimpleScreen(
        title = "Статистика",
        modifier = modifier
    )
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