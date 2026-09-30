package com.example.financetracker.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatCurrency(
    amount: Double,
    currency: String = "₽"
): String {

    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    }

    val formatter = DecimalFormat(
        "#,##0.##",
        symbols
    )

    return "${formatter.format(amount)} $currency"
}

