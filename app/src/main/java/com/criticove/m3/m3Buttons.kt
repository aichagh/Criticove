package com.criticove.m3

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

object ButtonStyles {
    private val ButtonPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    @Composable
    fun PrimaryButton(onClick: () -> Unit, text: String) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Use the primary color from the theme
                contentColor = MaterialTheme.colorScheme.onPrimary // Use the onPrimary color for text
            ),
            contentPadding = ButtonPadding
        ) {
            Text(text)
        }
    }

    @Composable
    fun SecondaryButton(onClick: () -> Unit, text: String) {
        FilledTonalButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary, // Use the secondary color from the theme
                contentColor = MaterialTheme.colorScheme.onSecondary // Use the onSecondary color for text
            ),
            contentPadding = ButtonPadding
        ) {
            Text(text)
        }
    }

    @Composable
    fun TertiaryButton(onClick: () -> Unit, text: String) {
        OutlinedButton(
            onClick = onClick,
            // OutlinedButton does not have a containerColor, it is typically transparent
            // If you want to set the border color to the tertiary color, that needs to be done with a border parameter (not shown here)
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.tertiary // Use the tertiary color for text
            ),
            contentPadding = ButtonPadding
        ) {
            Text(text)
        }
    }

    @Composable
    fun EditButton(onClick: () -> Unit, text: String) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = { Icon(Icons.Filled.Edit, contentDescription = "Edit") },
            text = { Text(text) }
        )
    }

    @Composable
    fun IconButton(onClick: () -> Unit, icon: ImageVector, description: String) {
        FilledIconButton(onClick = onClick) {
            Icon(icon, contentDescription = description)
        }
    }

    @Composable
    fun QuaternaryButton(onClick: () -> Unit, text: String) {
        ElevatedButton(
            onClick = onClick,
            // ElevatedButton uses elevation to stand out instead of containerColor
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant, // Use a color for elevation; surfaceVariant is an example
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant // Ensure readable text on surfaceVariant
            ),
            contentPadding = ButtonPadding
        ) {
            Text(text)
        }
    }

    @Composable
    fun QuinaryButton(onClick: () -> Unit, text: String) {
        TextButton(
            onClick = onClick,
            // TextButton typically has no background or border until pressed
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface // Use onSurface for text, which is designed to be readable on the background surface color
            ),
            contentPadding = ButtonPadding
        ) {
            Text(text)
        }
    }
}
