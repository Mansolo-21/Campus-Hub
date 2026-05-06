package com.nohari.campus_hub.Screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nohari.campus_hub.AppCard

@Composable
fun ActionTile(title: String, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(title)
    }
}