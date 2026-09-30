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
}
