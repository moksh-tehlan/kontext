package com.moksh.kontext.presentation.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OutlineDark

@Composable
fun UpgradeCard(
    onUpgradeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(width = 1.dp, color = OutlineDark, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text(
            text = "Want more Kontext?",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Upgrade for more usage and capabilities.",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Upgrade",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.background,
                ),
            )
        }
    }
}

@Composable
@Preview
fun UpgradeCardPreview() {
    KontextTheme { UpgradeCard(onUpgradeClick = {}) }
}