package com.moksh.kontext.presentation.screens.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.projectIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = projectIcon,
            contentDescription = "Project Icon",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Projects",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    KontextTheme {
        HomeHeader()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderDarkPreview() {
    KontextTheme(darkTheme = true) {
        HomeHeader()
    }
}
