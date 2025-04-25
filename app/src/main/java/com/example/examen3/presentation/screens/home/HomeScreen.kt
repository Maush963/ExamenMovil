package com.example.examen3.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.examen3.presentation.screens.home.components.SudokuBoard
import com.example.examen3.presentation.screens.home.components.DifficultySelector
import com.example.examen3.presentation.screens.home.components.GameStatusBanner
import com.example.examen3.presentation.screens.home.components.LoadingErrorOverlay
import com.example.examen3.presentation.screens.home.components.SudokuKeypad
import com.example.examen3.presentation.screens.home.components.ActionButtons

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel() // Inyecta el ViewModel
) {
    // Observa el estado de la UI del ViewModel de forma segura para el ciclo de vida
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Log general para ver cuándo se recompone y el estado general
    Log.d("HomeScreen", "Recomposing - isLoading: ${uiState.isLoading}, error: ${uiState.error}, sudoku is null: ${uiState.sudoku == null}, isCompleted: ${uiState.isGameCompleted}")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Padding general para toda la columna
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Alinea los elementos arriba por defecto
        ) {
            // --- Selector de Dificultad ---
            DifficultySelector(
                currentDifficulty = uiState.difficulty,
                onDifficultySelected = { newDifficulty ->
                    viewModel.changeDifficulty(newDifficulty)
                },
                modifier = Modifier.fillMaxWidth() // Ocupa el ancho disponible
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio debajo del selector

            // Log ANTES de llamar a SudokuBoard
            Log.d("HomeScreen", "Estado ANTES de SudokuBoard -> isLoading: ${uiState.isLoading}, error: ${uiState.error}, sudoku is null: ${uiState.sudoku == null}")

            // --- Tablero de Sudoku ---
            // Mantiene la proporción cuadrada y ocupa el ancho disponible
            SudokuBoard(
                sudoku = uiState.sudoku,
                selectedRow = uiState.selectedRow,
                selectedCol = uiState.selectedCol,
                onCellSelected = { row, col ->
                    viewModel.setSelectedCell(row, col)
                },
                modifier = Modifier
                    .fillMaxWidth() // Ocupa el ancho
                    .aspectRatio(1f) // Mantiene la proporción 1:1 (cuadrado)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio debajo del tablero

            // --- Teclado Numérico ---
            // Solo se muestra si hay un Sudoku cargado
            if (uiState.sudoku != null) {
                SudokuKeypad(
                    boardSize = uiState.sudoku?.size ?: uiState.boardSize,
                    onNumberSelected = { number ->
                        viewModel.updateCellValue(number)
                    },
                    modifier = Modifier.fillMaxWidth() // Ocupa el ancho disponible
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio debajo del teclado si se muestra
            }

            // --- Spacer Flexible ---
            // Este Spacer ocupa todo el espacio vertical restante, empujando
            // los elementos de abajo (ActionButtons, GameStatusBanner) hacia el fondo.
            Spacer(modifier = Modifier.weight(1f))

            // --- Botones de Acción ---
            ActionButtons(
                onNewGame = { viewModel.generateNewSudoku() },
                onResetGame = { viewModel.resetGame() },
                modifier = Modifier.fillMaxWidth() // Ocupa el ancho disponible
            )

            // --- Banner de Estado del Juego ---
            // Se muestra solo si el juego está completado
            if (uiState.isGameCompleted) {
                Spacer(modifier = Modifier.height(16.dp)) // Espacio encima del banner si se muestra
                GameStatusBanner(
                    isCompleted = true, // Asumiendo éxito si isGameCompleted es true
                    modifier = Modifier.fillMaxWidth() // Ocupa el ancho disponible
                )
            }
        }

        // Log ANTES de llamar a LoadingErrorOverlay
        Log.d("HomeScreen", "Estado ANTES de LoadingErrorOverlay -> isLoading: ${uiState.isLoading}, error: ${uiState.error}")

        // --- Overlay para Carga y Error ---
        // Cubre toda la pantalla si está activo (se dibuja encima de la Column)
        LoadingErrorOverlay(
            isLoading = uiState.isLoading,
            errorMessage = uiState.error,
            onRetry = { viewModel.generateNewSudoku() } // Reintenta generando un nuevo Sudoku
        )
    }
}