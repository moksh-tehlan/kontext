package com.moksh.kontext.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.core.theme.AuthTextSecondary
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.theme.OutlineDark

@Composable
fun KontextTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundColor: Color = Color.Transparent,
    borderColor: Color = OutlineDark,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = AuthTextSecondary
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = textColor
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        cursorBrush = SolidColor(textColor),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = placeholderColor
                    )
                }
                innerTextField()
            }
        }
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