package com.moksh.kontext.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OutlineDark
import com.moksh.kontext.presentation.core.theme.TextFieldBackground

@Composable
fun KontextTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundColor: Color = TextFieldBackground,
    borderColor: Color = OutlineDark,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign = TextAlign.Start,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    enabled: Boolean = true,
    maxLines: Int = 1,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = textColor,
            textAlign = textAlign
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = textAlign
                    ),
                    color = placeholderColor
                )
        },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = borderColor,
            unfocusedTextColor = placeholderColor,
            disabledTextColor = placeholderColor,
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = if (maxLines == 1) true else false,
        maxLines = maxLines,
        enabled = enabled
    )
}

@Preview(showBackground = true)
@Composable
private fun KontextTextFieldPreview() {
    KontextTheme {
        KontextTextField(
            value = "",
            onValueChange = {},
            placeholder = "name@yourcompany.com"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KontextTextFieldWithValuePreview() {
    KontextTheme {
        KontextTextField(
            value = "user@example.com",
            onValueChange = {},
            placeholder = "name@yourcompany.com"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun KontextTextFieldDarkPreview() {
    KontextTheme(darkTheme = true) {
        KontextTextField(
            value = "user@example.com",
            onValueChange = {},
            placeholder = "name@yourcompany.com"
        )
    }
}