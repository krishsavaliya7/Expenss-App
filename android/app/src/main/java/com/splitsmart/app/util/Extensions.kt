package com.splitsmart.app.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

fun Double.formatCurrency(currencyCode: String = "INR"): String = try {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    format.format(this)
} catch (e: Exception) { "${Constants.currencySymbol(currencyCode)}${"%.2f".format(this)}" }

fun String?.formatDateTime(): String {
    if (this == null) return ""
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        output.format(input.parse(this)!!)
    } catch (e: Exception) { this }
}

fun String?.formatDate(): String {
    if (this == null) return ""
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        output.format(input.parse(this)!!)
    } catch (e: Exception) { this }
}

fun String.initials(): String = split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("").ifEmpty { take(1).uppercase() }
