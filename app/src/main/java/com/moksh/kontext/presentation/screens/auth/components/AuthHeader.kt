package com.moksh.kontext.presentation.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.R
import com.moksh.kontext.presentation.core.theme.AuthTextPrimary
import com.moksh.kontext.presentation.core.theme.AuthTextSecondary
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun AuthHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kontext logo placeholder - you can add the actual logo here
        Text(
            text = stringResource(R.string.app_logo),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = AuthTextPrimary
        )
        
        Text(
            text = stringResource(R.string.auth_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = AuthTextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Text(
            text = stringResource(R.string.auth_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = AuthTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthHeaderPreview() {
    KontextTheme(darkTheme = true) {
        AuthHeader()
    }
}
