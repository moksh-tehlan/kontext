package com.moksh.kontext.presentation.screens.knowledge_source.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moksh.kontext.domain.model.KnowledgeSourceDto
import com.moksh.kontext.domain.model.KnowledgeSourceStatus
import com.moksh.kontext.domain.model.KnowledgeSourceType
import com.moksh.kontext.presentation.common.documentIcon
import com.moksh.kontext.presentation.common.webIcon
import com.moksh.kontext.presentation.core.theme.KontextTheme
import com.moksh.kontext.presentation.core.utils.DateUtils

@Composable
fun KnowledgeSourceItem(
    knowledgeSource: KnowledgeSourceDto,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon based on type
        Icon(
            imageVector = when (knowledgeSource.type) {
                KnowledgeSourceType.DOCUMENT -> documentIcon
                KnowledgeSourceType.WEB -> webIcon
            },
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = knowledgeSource.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = DateUtils.formatDateString(knowledgeSource.updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                // Status indicator
                when (knowledgeSource.status) {
                    KnowledgeSourceStatus.PROCESSING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    KnowledgeSourceStatus.SUCCESS -> {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }

                    KnowledgeSourceStatus.ERROR -> {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Delete button
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KnowledgeSourceItemPreview() {
    KontextTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KnowledgeSourceItem(
                knowledgeSource = KnowledgeSourceDto(
                    id = "1",
                    name = "research_publication_on_...",
                    type = KnowledgeSourceType.DOCUMENT,
                    createdAt = "2025-05-30T12:00:00Z",
                    updatedAt = "2025-05-30T12:00:00Z",
                    status = KnowledgeSourceStatus.SUCCESS
                ),
                onDelete = {}
            )
            KnowledgeSourceItem(
                knowledgeSource = KnowledgeSourceDto(
                    id = "2",
                    name = "priority.md",
                    type = KnowledgeSourceType.DOCUMENT,
                    createdAt = "2025-05-30T12:00:00Z",
                    updatedAt = "2025-05-30T12:00:00Z",
                    status = KnowledgeSourceStatus.PROCESSING
                ),
                onDelete = {}
            )
            KnowledgeSourceItem(
                knowledgeSource = KnowledgeSourceDto(
                    id = "3",
                    name = "example.com",
                    type = KnowledgeSourceType.WEB,
                    source = "https://example.com",
                    createdAt = "2025-05-30T12:00:00Z",
                    updatedAt = "2025-05-30T12:00:00Z",
                    status = KnowledgeSourceStatus.ERROR
                ),
                onDelete = {}
            )
        }
    }
} 