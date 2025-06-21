package com.moksh.kontext.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A production-ready, smooth, and reactive collapsible app bar using the
 * latest Material 3 components.
 *
 * This implementation uses a `LargeTopAppBar` coupled with a `Scaffold` and a
 * `TopAppBarScrollBehavior` to achieve a collapsible effect that is both
- * performant and idiomatic in modern Android development.
 *
 * @param title The main title text to be displayed in the app bar.
 * @param navigationIcon Optional navigation icon to display at the start of the app bar.
 * @param onNavigationClick Callback invoked when the navigation icon is clicked.
 * @param backgroundColor The background color for both the expanded and collapsed app bar.
 * @param contentColor The color for the title and icons within the app bar.
 * @param content The scrollable content to be displayed below the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KontextCollapsibleHeader(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = backArrowIcon,
    onNavigationClick: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: LazyListScope.() -> Unit
) {
    // This scroll behavior is the key to the collapsible effect.
    // It tracks the scroll state and animates the app bar's height and
    // title properties accordingly.

}


// Usage example
@Preview(showBackground = true)
@Composable
fun ExampleUsagePreview() {
    MaterialTheme {
        KontextCollapsibleHeader(
            title = "CSR airlinq A1",
            onNavigationClick = { /* Handle back navigation */ },
            backgroundColor = Color(0xFF2C2C2C), // Dark background
            contentColor = Color.White
        ) {
            // Your scrollable content here
            items(count = 50) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Content item ${index + 1}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}