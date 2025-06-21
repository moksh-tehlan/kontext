package com.moksh.kontext.presentation.screens.otp.components

import android.opengl.Visibility
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.R
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.KontextTextField
import com.moksh.kontext.presentation.common.sendIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OutlineDark

@Composable
fun OtpForm(
    email:String,
    otp: String,
    isVerifying: Boolean = false,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String?=null,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.large)
        .border(2.dp, OutlineDark, shape = MaterialTheme.shapes.large)
        .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = sendIcon,
                contentDescription = "otp icon",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "If you have received a code you can enter it",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            KontextTextField(
                value = otp,
                enabled = !isVerifying,
                onValueChange = onOtpChange,
                placeholder = "000000",
                keyboardType = KeyboardType.Number,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp)).padding(20.dp),
                visible = error != null && error.isNotEmpty(),
                content = {
                    Text(
                        text = error ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "We have sent you an OTP to",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpFormPreview() {
    KontextTheme(darkTheme = true) {
        OtpForm(
            email = "someone@gmail.com",
            otp = "",
            onOtpChange = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpFormCompletePreview() {
    KontextTheme(darkTheme = true) {
        OtpForm(
            email = "someone@gmail.com",
            otp = "123456",
            onOtpChange = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpFormLoadingPreview() {
    KontextTheme(darkTheme = true) {
        OtpForm(
            email = "someone@gmail.com",
            isVerifying = true,
            otp = "123456",
            onOtpChange = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun OtpFormErrorPreview() {
    KontextTheme(darkTheme = true) {
        OtpForm(
            email = "someone@gmail.com",
            otp = "123456",
            onOtpChange = {},
            modifier = Modifier.padding(24.dp),
            error = "An error has occurred. Please try again."
        )
    }
}
