package com.example.examen3.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameStatusBanner(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isCompleted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isCompleted) "¡Felicidades! Sudoku completado correctamente"
            else "Hay errores en tu solución",
            color = if (isCompleted) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onError
        )
    }
}