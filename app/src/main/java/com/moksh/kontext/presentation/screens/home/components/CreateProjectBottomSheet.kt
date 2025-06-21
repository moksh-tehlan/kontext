package com.moksh.kontext.presentation.screens.home.components

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
fun CreateProjectBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    isLoading: Boolean = false,
    name: String = "",
    description: String = "",
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCreateProject: () -> Unit,
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
                    text = "Create a project",
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

            // Form fields section
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Project name field
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "What are you working on?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )

                    KontextTextField(
                        value = name,
                        onValueChange = onNameChange,
                        placeholder = "Name your project",
                        keyboardType = KeyboardType.Text,
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        borderColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Project description field
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "What are you trying to achieve?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )

                    // Multi-line text field for description
                    KontextTextField(
                        value = description,
                        onValueChange = onDescriptionChange,
                        placeholder = "Describe your project, goals, subject, etc...",
                        keyboardType = KeyboardType.Text,
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        borderColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5,
                    )
                }
            }

            // Create project button
            KontextButton(
                text = "Create project",
                onClick = {
                    if (name.isNotBlank() && description.isNotBlank()) {
                        onCreateProject()
                    }
                },
                isLoading = isLoading,
                enabled = name.isNotBlank() && description.isNotBlank(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CreateProjectBottomSheetPreview() {
    KontextTheme {
        CreateProjectBottomSheet(
            onDismiss = { },
            isLoading = false,
            name = "",
            description = "",
            onNameChange = {},
            onDescriptionChange = {},
            onCreateProject = {},
            sheetState = androidx.compose.material3.rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CreateProjectBottomSheetDarkPreview() {
    KontextTheme(darkTheme = true) {
        CreateProjectBottomSheet(
            onDismiss = { },
            isLoading = false,
            name = "",
            description = "",
            onNameChange = {},
            onDescriptionChange = {},
            onCreateProject = {},
            sheetState = androidx.compose.material3.rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        )
    }
}