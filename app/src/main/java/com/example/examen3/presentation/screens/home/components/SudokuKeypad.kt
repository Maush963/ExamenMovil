package com.example.examen3.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SudokuKeypad(
    boardSize: Int, // Puede ser útil para lógica futura o botones adicionales
    onNumberSelected: (Int) -> Unit,
    // Añade aquí otros callbacks si los necesitas, por ejemplo:
    // onEraseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp), // Padding horizontal opcional
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Primera Fila (Números 1-5) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Distribuye botones equitativamente
        ) {
            (1..5).forEach { number ->
                Button(onClick = { onNumberSelected(number) }) {
                    Text(text = "$number")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre las filas

        // --- Segunda Fila (Números 6-9 y otros botones opcionales) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround // Distribuye botones equitativamente
        ) {
            (6..9).forEach { number ->
                Button(onClick = { onNumberSelected(number) }) {
                    Text(text = "$number")
                }
            }
        }
    }
}