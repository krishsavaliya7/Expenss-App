package com.splitsmart.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.splitsmart.app.data.api.models.GroupData
import com.splitsmart.app.ui.theme.ErrorRed
import com.splitsmart.app.ui.theme.SuccessGreen
import com.splitsmart.app.util.formatCurrency

@Composable
fun GroupCard(group: GroupData, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AvatarCircle(text = group.groupName, size = 48)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(text = group.groupName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Group, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(" ${group.memberCount ?: 0} members", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (group.balance != null && group.balance != 0.0) {
                Text(text = (if (group.balance > 0) "+" else "") + group.balance.formatCurrency(group.currency), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (group.balance > 0) SuccessGreen else ErrorRed)
            }
        }
    }
}
