package com.moksh.kontext.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.backArrowIcon
import com.moksh.kontext.presentation.common.billingIcon
import com.moksh.kontext.presentation.common.hapticFeedback
import com.moksh.kontext.presentation.common.infoIcon
import com.moksh.kontext.presentation.common.logoutIcon
import com.moksh.kontext.presentation.common.projectIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.screens.settings.components.AccountTier
import com.moksh.kontext.presentation.screens.settings.components.SettingsItem
import com.moksh.kontext.presentation.screens.settings.components.UpgradeCard

@Composable
fun SettingsScreen() {
    SettingsScreenView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenView() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Settings",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = backArrowIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = infoIcon,
                            contentDescription = "Navigation back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AccountTier(
                email = "someone@gmail.com",
                tier = "Free",
            )
            UpgradeCard(
                onUpgradeClick = {}
            )
            SettingsItem(
                title = "Profile",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = projectIcon,
                        contentDescription = "profile icon"
                    )
                },
                onClick = { }
            )
            SettingsItem(
                title = "Billing",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = billingIcon,
                        contentDescription = "profile icon"
                    )
                },
                onClick = { }
            )
            HorizontalDivider()
            SettingsItem(
                title = "Haptic feedback",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = hapticFeedback,
                        contentDescription = "profile icon"
                    )
                },
                onClick = { }
            )
            HorizontalDivider()
            SettingsItem(
                title = "Logout",
                textColor = MaterialTheme.colorScheme.error,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error,
                        imageVector = logoutIcon,
                        contentDescription = "profile icon"
                    )
                },
                onClick = { }
            )
        }
    }
}

@Composable
@Preview
private fun SettingsScreenPreview() {
    KontextTheme { SettingsScreenView() }
}