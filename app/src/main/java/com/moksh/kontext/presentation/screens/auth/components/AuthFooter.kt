package com.moksh.kontext.presentation.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moksh.kontext.R
import com.moksh.kontext.presentation.core.theme.AuthTextSecondary
import com.moksh.kontext.presentation.core.theme.KontextTheme
import java.nio.file.WatchEvent

@Composable
fun AuthFooter(
    onTermsClick: () -> Unit,
    onUsagePolicyClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val termsText = buildAnnotatedString {
            append("By continuing, you agree to Anthropic's ")
            
            pushStringAnnotation(tag = "consumer_terms", annotation = "consumer_terms")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(stringResource(R.string.consumer_terms))
            }
            pop()
            
            append(" and ")
            
            pushStringAnnotation(tag = "usage_policy", annotation = "usage_policy")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(stringResource(R.string.usage_policy))
            }
            pop()
            
            append(", and acknowledge their ")
            
            pushStringAnnotation(tag = "privacy_policy", annotation = "privacy_policy")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(stringResource(R.string.privacy_policy))
            }
            pop()
            
            append(".")
        }
        ClickableText(
            text = termsText,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            ),
            onClick = { offset ->
                termsText.getStringAnnotations(tag = "consumer_terms", start = offset, end = offset)
                    .firstOrNull()?.let { onTermsClick() }
                
                termsText.getStringAnnotations(tag = "usage_policy", start = offset, end = offset)
                    .firstOrNull()?.let { onUsagePolicyClick() }
                
                termsText.getStringAnnotations(tag = "privacy_policy", start = offset, end = offset)
                    .firstOrNull()?.let { onPrivacyPolicyClick() }
            }
        )
        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthFooterPreview() {
    KontextTheme(darkTheme = true) {
        AuthFooter(
            onTermsClick = {},
            onUsagePolicyClick = {},
            onPrivacyPolicyClick = {}
        )
    }
}
