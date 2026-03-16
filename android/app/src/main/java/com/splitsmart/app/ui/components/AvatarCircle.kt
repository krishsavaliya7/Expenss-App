package com.splitsmart.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.splitsmart.app.util.initials

@Composable
fun AvatarCircle(text: String, size: Int = 40, imageUrl: String? = null, modifier: Modifier = Modifier) {
    if (imageUrl != null) {
        AsyncImage(model = imageUrl, contentDescription = text, modifier = modifier.size(size.dp).clip(CircleShape), contentScale = ContentScale.Crop)
    } else {
        Box(modifier = modifier.size(size.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Text(text = text.initials(), style = MaterialTheme.typography.titleSmall.copy(fontSize = (size / 3).sp), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
