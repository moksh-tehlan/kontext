package com.moksh.kontext.presentation.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.presentation.common.KontextButton
import com.moksh.kontext.presentation.common.KontextTextField
import com.moksh.kontext.presentation.core.theme.KontextTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInstructionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    instruction: String = "",
    onInstructionChange: (String) -> Unit,
    onSaveInstruction: () -> Unit,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header with title and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Set custom instructions",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Subtitle
            Text(
                text = "Instruct Claude how to behave and respond for all of the chats within JAVA DSA Expert",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )

            // Instruction text field
            KontextTextField(
                value = instruction,
                onValueChange = onInstructionChange,
                placeholder = "you are JAVA and DSA expert always give ur code in java format only",
                keyboardType = KeyboardType.Text,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                borderColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
            )

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KontextButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    enabled = !isLoading
                )

                KontextButton(
                    text = "Save",
                    onClick = {
                        if (instruction.isNotBlank()) {
                            onSaveInstruction()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    isLoading = isLoading,
                    enabled = instruction.isNotBlank(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CustomInstructionDialogPreview() {
    KontextTheme {
        CustomInstructionDialog(
            onDismiss = { },
            isLoading = false,
            instruction = "",
            onInstructionChange = {},
            onSaveInstruction = {},
            sheetState = androidx.compose.material3.rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CustomInstructionDialogDarkPreview() {
    KontextTheme(darkTheme = true) {
        CustomInstructionDialog(
            onDismiss = { },
            isLoading = false,
            instruction = "you are JAVA and DSA expert always give ur code in java format only",
            onInstructionChange = {},
            onSaveInstruction = {},
            sheetState = androidx.compose.material3.rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        )
    }
} 