package com.splitsmart.app.util

object Constants {
    val CURRENCIES = listOf("INR" to "₹", "USD" to "$", "EUR" to "€", "GBP" to "£", "AUD" to "A$")
    fun currencySymbol(code: String) = CURRENCIES.firstOrNull { it.first == code }?.second ?: code
    val EXPENSE_CATEGORIES = listOf("Food & Drinks", "Transport", "Shopping", "Entertainment", "Bills & Utilities", "Rent", "Healthcare", "Education", "Travel", "Other")
    val SPLIT_TYPES = listOf("equal", "exact", "percentage")
}
