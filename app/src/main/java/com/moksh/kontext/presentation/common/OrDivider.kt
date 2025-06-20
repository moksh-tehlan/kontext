package com.moksh.kontext.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.R
import com.moksh.kontext.presentation.core.theme.AuthTextSecondary
import com.moksh.kontext.presentation.core.theme.KontextTheme

@Composable
fun OrDivider(
    modifier: Modifier = Modifier,
    dividerColor: Color = AuthTextSecondary.copy(alpha = 0.3f),
    textColor: Color = AuthTextSecondary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(dividerColor)
        )
        
        Text(
            text = stringResource(R.string.or_divider),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(dividerColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrDividerPreview() {
    KontextTheme {
        OrDivider()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OrDividerDarkPreview() {
    KontextTheme(darkTheme = true) {
        OrDivider()
    }
}
