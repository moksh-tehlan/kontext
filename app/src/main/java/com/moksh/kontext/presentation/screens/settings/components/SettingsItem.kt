package com.moksh.kontext.presentation.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.projectIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.invoke()
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = title, style = MaterialTheme.typography.titleLarge.copy(
                color = textColor
            )
        )
    }
}

@Composable
@Preview
private fun SettingsIconPreview() {
    KontextTheme {
        SettingsItem(
            title = "Settings Item",
            icon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = projectIcon,
                    contentDescription = "project icon"
                )
            },
            onClick = {}
        )
    }
}