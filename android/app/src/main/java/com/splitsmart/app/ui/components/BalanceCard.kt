package com.splitsmart.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.ui.theme.ErrorRed
import com.splitsmart.app.ui.theme.SuccessGreen
import com.splitsmart.app.util.formatCurrency

@Composable
fun BalanceCard(label: String, amount: Double, currencyCode: String = "INR", modifier: Modifier = Modifier) {
    val color = if (amount >= 0) SuccessGreen else ErrorRed
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = kotlin.math.abs(amount).formatCurrency(currencyCode), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun BalanceSummaryRow(youOwe: Double, youAreOwed: Double, currencyCode: String = "INR", modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BalanceCard("You Owe", -youOwe, currencyCode, Modifier.weight(1f))
        BalanceCard("You're Owed", youAreOwed, currencyCode, Modifier.weight(1f))
    }
}
