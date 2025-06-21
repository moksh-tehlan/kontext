package com.moksh.kontext.presentation.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.rightArrowIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun ProjectItem(
    modifier: Modifier = Modifier,
    projectName: String,
    date: String = "21 June 2025",
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = projectName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = rightArrowIcon,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                contentDescription = "Project Icon",
            )
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = date,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
        )
    }
}

@Preview
@Composable
fun ProjectItemPreview() {
    KontextTheme {
        ProjectItem(
            projectName = "Sample Project",
            onClick = {}
        )
    }
}