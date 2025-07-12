package com.moksh.kontext.presentation.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.moksh.kontext.domain.model.ChatMessageDto
import com.moksh.kontext.domain.model.MessageType

@Composable
fun MessageItem(
    message: ChatMessageDto,
    userNickname: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        when (message.type) {
            MessageType.USER -> {
                UserMessage(
                    message = message,
                    userNickname = userNickname
                )
            }

            MessageType.ASSISTANT -> {
                AssistantMessage(
                    message = message
                )
            }
        }
    }
}

@Composable
private fun UserMessage(
    message: ChatMessageDto,
    userNickname: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // User nickname
        Text(
            text = userNickname,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // User message in a box
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun AssistantMessage(
    message: ChatMessageDto,
    modifier: Modifier = Modifier
) {
    // Assistant message with beautiful markdown rendering that follows app theme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        ParseAndRenderMarkdown(message.content)
    }
}

@Composable
private fun ParseAndRenderMarkdown(content: String) {
    val lines = content.split('\n')
    var inCodeBlock = false
    var codeBlockLanguage = ""
    val codeBlockLines = mutableListOf<String>()

    for (line in lines) {
        when {
            // Code block start/end
            line.startsWith("```") -> {
                if (!inCodeBlock) {
                    // Starting code block
                    inCodeBlock = true
                    codeBlockLanguage = line.substring(3).trim()
                    codeBlockLines.clear()

                    // Show language label if specified
                    if (codeBlockLanguage.isNotEmpty()) {
                        Text(
                            text = codeBlockLanguage,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                        )
                    }
                } else {
                    // Ending code block - render all collected lines
                    if (codeBlockLines.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                codeBlockLines.forEach { codeLine ->
                                    Text(
                                        text = codeLine,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                    inCodeBlock = false
                    codeBlockLanguage = ""
                    codeBlockLines.clear()
                }
            }
            // Inside code block
            inCodeBlock -> {
                codeBlockLines.add(line)
            }
            // Headers
            line.startsWith("# ") -> {
                Text(
                    text = line.substring(2),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            line.startsWith("## ") -> {
                Text(
                    text = line.substring(3),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            line.startsWith("### ") -> {
                Text(
                    text = line.substring(4),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            // Lists
            line.startsWith("- ") || line.startsWith("* ") -> {
                Text(
                    text = "• ${line.substring(2)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                )
            }
            // Regular text lines
            line.trim().isNotEmpty() -> {
                val styledText = processInlineMarkdown(line)
                Text(
                    text = styledText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            // Empty line
            line.isBlank() -> {
                Text(
                    text = "",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Handle case where code block wasn't closed
    if (inCodeBlock && codeBlockLines.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                codeBlockLines.forEach { codeLine ->
                    Text(
                        text = codeLine,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun processInlineMarkdown(text: String): AnnotatedString {
    // Simple check for code blocks vs regular text
    if (text.contains("```")) return buildAnnotatedString { append(text) }

    return buildAnnotatedString {
        var currentIndex = 0
        val content = text

        // Handle **bold** text
        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
        val boldMatches = boldRegex.findAll(content).toList()

        // Handle `code` text
        val codeRegex = Regex("`(.*?)`")
        val codeMatches = codeRegex.findAll(content).toList()

        // Combine and sort all matches
        val allMatches = (boldMatches + codeMatches).sortedBy { it.range.first }

        for (match in allMatches) {
            // Add text before match
            if (currentIndex < match.range.first) {
                append(content.substring(currentIndex, match.range.first))
            }

            // Add styled match
            when {
                match.value.startsWith("**") -> {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        append(match.groupValues[1])
                    }
                }

                match.value.startsWith("`") -> {
                    withStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            background = MaterialTheme.colorScheme.surfaceVariant,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(match.groupValues[1])
                    }
                }
            }

            currentIndex = match.range.last + 1
        }

        // Add remaining text
        if (currentIndex < content.length) {
            append(content.substring(currentIndex))
        }
    }
}

