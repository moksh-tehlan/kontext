package com.moksh.kontext.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OnPrimary
import com.moksh.kontext.presentation.core.theme.Primary

@Composable
fun KontextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundColor: Color = Primary,
    textColor: Color = OnPrimary,
    disabledBackgroundColor: Color = backgroundColor.copy(alpha = 0.5f),
    disabledTextColor: Color = textColor.copy(alpha = 0.5f)
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledTextColor
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.padding(2.dp)
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KontextButtonPreview() {
    KontextTheme {
        KontextButton(
            text = "Continue with Email",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KontextButtonLoadingPreview() {
    KontextTheme {
        KontextButton(
            text = "Continue with Email",
            onClick = {},
            isLoading = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KontextButtonDisabledPreview() {
    KontextTheme {
        KontextButton(
            text = "Continue with Email",
            onClick = {},
            enabled = false
        )
    }
}
