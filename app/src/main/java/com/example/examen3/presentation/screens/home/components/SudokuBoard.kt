package com.example.examen3.presentation.screens.home.components

import android.util.Log // Importa Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.examen3.domain.model.Sudoku

@Composable
fun SudokuBoard(
    sudoku: Sudoku?,
    selectedRow: Int,
    selectedCol: Int,
    onCellSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Log para verificar si se llama y qué recibe
    Log.d("SudokuBoard", "Composable llamado. Sudoku is null: ${sudoku == null}")

    if (sudoku == null) {
        Log.d("SudokuBoard", "Sudoku es null, no se dibuja nada.")
        // Si el sudoku es null, no hacemos nada más en este Composable
        return
    }

    // Log para verificar el contenido del estado si el sudoku NO es null
    // Imprime el tamaño y las primeras 2 filas como ejemplo
    Log.d("SudokuBoard", "Sudoku recibido. Size: ${sudoku.size}. Primeras filas (currentState): ${sudoku.currentState.take(2)}")
    // También puedes loguear el initialBoard si quieres compararlo
    // Log.d("SudokuBoard", "Primeras filas (initialBoard): ${sudoku.initialBoard.take(2)}")

    val boardSize = sudoku.currentState.size

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Mantiene la proporción cuadrada
            .border(2.dp, MaterialTheme.colorScheme.primary) // Borde exterior grueso
    ) {
        Column {
            for (row in 0 until boardSize) {
                Row(
                    modifier = Modifier.weight(1f) // Cada fila ocupa el mismo alto
                ) {
                    for (col in 0 until boardSize) {
                        val isSelected = row == selectedRow && col == selectedCol
                        val value = sudoku.currentState[row][col]
                        val isEditable = sudoku.isEditable(row, col) // Usa la función del modelo

                        SudokuCell(
                            value = value,
                            isSelected = isSelected,
                            isEditable = isEditable,
                            onCellClick = { onCellSelected(row, col) },
                            modifier = Modifier.weight(1f) // Cada celda ocupa el mismo ancho
                        )
                    }
                }
            }
        }
    }
}