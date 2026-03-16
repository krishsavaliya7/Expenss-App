package com.splitsmart.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splitsmart.app.data.api.models.FriendData

@Composable
fun FriendItem(friend: FriendData, onSendRequest: (() -> Unit)? = null, onAccept: (() -> Unit)? = null, onReject: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AvatarCircle(text = friend.fullName ?: friend.username, size = 44)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(friend.fullName ?: friend.username, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("@${friend.username}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            when (friend.requestStatus) {
                "none" -> onSendRequest?.let { FilledTonalIconButton(onClick = it) { Icon(Icons.Filled.PersonAdd, "Send request") } }
                "pending_received" -> {
                    onAccept?.let { IconButton(onClick = it) { Icon(Icons.Filled.Check, "Accept", tint = MaterialTheme.colorScheme.primary) } }
                    onReject?.let { IconButton(onClick = it) { Icon(Icons.Filled.Close, "Reject", tint = MaterialTheme.colorScheme.error) } }
                }
                "pending_sent" -> Text("Pending", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                else -> {}
            }
        }
    }
}
