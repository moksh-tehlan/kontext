package com.moksh.kontext.presentation.screens.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.openViewIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun InfoDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onConsumerTermsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onHelpSupportClick: () -> Unit = {},
    appVersion: String = "1.0"
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = 0.dp, y = 8.dp),
        modifier = Modifier.fillMaxWidth(fraction = 0.6f),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        // Consumer Terms
        DropdownMenuItem(
            text = {
                Text(
                    text = "Consumer Terms",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onConsumerTermsClick()
                onDismiss()
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = openViewIcon,
                    contentDescription = "Open Consumer Terms",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        // Privacy Policy
        DropdownMenuItem(
            text = {
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onPrivacyPolicyClick()
                onDismiss()
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = openViewIcon,
                    contentDescription = "Open Privacy Policy",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        // First Horizontal Divider
        HorizontalDivider()

        // Help & Support
        DropdownMenuItem(
            text = {
                Text(
                    text = "Help & Support",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = {
                onHelpSupportClick()
                onDismiss()
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = openViewIcon,
                    contentDescription = "Open Help & Support",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        // Second Horizontal Divider
        HorizontalDivider()

        // Version (non-clickable)
        DropdownMenuItem(
            text = {
                Text(
                    text = "Version $appVersion",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            onClick = { /* Non-clickable version item */ }
        )
    }
}

@Composable
@Preview
private fun InfoDropdownMenuPreview() {
    KontextTheme {
        InfoDropdownMenu(
            expanded = true,
            onDismiss = {},
            appVersion = "1.0"
        )
    }
}