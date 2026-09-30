package com.example.financetracker.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val preferences =
        application.getSharedPreferences(
            "finance_settings",
            Application.MODE_PRIVATE
        )

    // Валюта
    private val _currency = MutableStateFlow(
        preferences.getString("currency", "₽") ?: "₽"
    )

    val currency: StateFlow<String> = _currency

    fun setCurrency(currency: String) {
        _currency.value = currency

        viewModelScope.launch {
            preferences.edit()
                .putString("currency", currency)
                .apply()
        }
    }

    // Месячный бюджет
    private val _monthlyBudget = MutableStateFlow(
        preferences.getString("monthly_budget", null)?.toDoubleOrNull()
    )

    val monthlyBudget: StateFlow<Double?> = _monthlyBudget

    fun setMonthlyBudget(budget: Double?) {
        _monthlyBudget.value = budget

        viewModelScope.launch {
            val editor = preferences.edit()

            if (budget == null) {
                editor.remove("monthly_budget")
            } else {
                editor.putString("monthly_budget", budget.toString())
            }

            editor.apply()
        }
    }
}

