package com.moksh.kontext.presentation.screens.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.R
import com.moksh.kontext.presentation.common.GoogleSignInButton
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.KontextTextField
import com.moksh.kontext.presentation.common.OrDivider
import com.moksh.kontext.presentation.core.theme.AuthTextPrimary
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OnPrimary
import com.moksh.kontext.presentation.core.theme.OutlineDark
import com.moksh.kontext.presentation.core.theme.Primary

@Composable
fun AuthForm(
    email: String,
    onEmailChange: (String) -> Unit,
    onContinueWithEmail: () -> Unit,
    onContinueWithGoogle: () -> Unit,
    isEmailLoading: Boolean,
    isGoogleLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        KontextTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.email_placeholder),
            keyboardType = KeyboardType.Email,
            textColor = AuthTextPrimary,
            borderColor = OutlineDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        KontextButton(
            text = stringResource(R.string.continue_with_email),
            onClick = onContinueWithEmail,
            isLoading = isEmailLoading,
            backgroundColor = Primary,
            textColor = OnPrimary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        OrDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        GoogleSignInButton(
            onClick = onContinueWithGoogle,
            isLoading = isGoogleLoading,
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = AuthTextPrimary,
            borderColor = OutlineDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthFormPreview() {
    KontextTheme(darkTheme = true) {
        AuthForm(
            email = "user@example.com",
            onEmailChange = {},
            onContinueWithEmail = {},
            onContinueWithGoogle = {},
            isEmailLoading = false,
            isGoogleLoading = false
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun AuthFormLoadingPreview() {
    KontextTheme(darkTheme = true) {
        AuthForm(
            email = "user@example.com",
            onEmailChange = {},
            onContinueWithEmail = {},
            onContinueWithGoogle = {},
            isEmailLoading = true,
            isGoogleLoading = false
        )
    }
}
