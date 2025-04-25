package com.example.examen3.presentation.screens.home

import android.util.Log // Importa Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examen3.domain.model.Difficulty
import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.usecase.GenerateSudokuUseCase
import com.example.examen3.domain.usecase.LoadSudokuProgressUseCase
import com.example.examen3.domain.usecase.ResetGameUseCase
import com.example.examen3.domain.usecase.SaveSudokuProgressUseCase
import com.example.examen3.domain.usecase.VerifySudokuUseCase
// Asegúrate que este import sea correcto si tienes tu propia clase Result
// import com.example.examen3.presentation.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch // Importa catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val generateSudokuUseCase: GenerateSudokuUseCase,
    private val verifySudokuUseCase: VerifySudokuUseCase,
    private val saveSudokuProgressUseCase: SaveSudokuProgressUseCase,
    private val loadSudokuProgressUseCase: LoadSudokuProgressUseCase,
    private val resetGameUseCase: ResetGameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        Log.d("HomeViewModel", "Inicializando y cargando juego guardado...")
        loadSavedGame()
    }

    private fun loadSavedGame() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // Inicia carga, limpia error previo
            try {
                Log.d("HomeViewModel", "Intentando cargar desde LoadSudokuProgressUseCase")
                val sudoku = loadSudokuProgressUseCase()
                if (sudoku != null) {
                    Log.d("HomeViewModel", "Sudoku cargado desde preferencias.")
                    _uiState.update {
                        it.copy(
                            sudoku = sudoku,
                            isLoading = false,
                            error = null,
                            isGameCompleted = verifySudokuUseCase(sudoku)
                        )
                    }
                } else {
                    Log.d("HomeViewModel", "No hay Sudoku guardado, generando uno nuevo...")
                    // No actualices isLoading aquí, generateNewSudoku lo hará
                    generateNewSudoku()
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al cargar Sudoku guardado", e)
                _uiState.update { it.copy(error = "Error al cargar: ${e.message}", isLoading = false, sudoku = null) }
                // Intenta generar uno nuevo incluso si la carga falló catastróficamente
                // generateNewSudoku() // Considera si quieres generar uno nuevo aquí o solo mostrar el error
            }
        }
    }


    fun generateNewSudoku() {
        val difficulty = _uiState.value.difficulty
        val boardSize = _uiState.value.boardSize
        Log.d("HomeViewModel", "Generando nuevo Sudoku - Tamaño: $boardSize, Dificultad: $difficulty")

        viewModelScope.launch {
            // Asegúrate de que isLoading esté en true y el error limpio ANTES de colectar el flow
            _uiState.update { it.copy(isLoading = true, error = null, sudoku = null) } // Limpia sudoku anterior

            try {
                generateSudokuUseCase(boardSize, difficulty)
                    .catch { e -> // Captura errores DENTRO del flow
                        Log.e("HomeViewModel", "Error en el flow de generateSudokuUseCase", e)
                        _uiState.update {
                            it.copy(
                                error = "Error generando: ${e.message}",
                                isLoading = false,
                                sudoku = null
                            )
                        }
                        // Emitir un valor o completar el flow aquí si es necesario,
                        // aunque catch ya termina la colección normalmente.
                    }
                    .collect { kotlinResult ->
                        kotlinResult.fold(
                            onSuccess = { sudoku ->
                                Log.d("HomeViewModel", "Sudoku generado con éxito.")
                                _uiState.update {
                                    it.copy(
                                        sudoku = sudoku,
                                        isLoading = false,
                                        error = null,
                                        isGameCompleted = false,
                                        selectedRow = -1,
                                        selectedCol = -1
                                    )
                                }
                                // Guarda el nuevo Sudoku generado
                                saveSudokuProgressUseCase(sudoku)
                            },
                            onFailure = { error ->
                                Log.e("HomeViewModel", "Fallo al generar Sudoku (onFailure): ${error.message}")
                                _uiState.update {
                                    it.copy(
                                        error = error.message ?: "Error desconocido al generar",
                                        isLoading = false,
                                        sudoku = null
                                    )
                                }
                            }
                        )
                    }
            } catch (e: Exception) {
                // Captura excepciones fuera del flow (poco probable con .catch, pero por seguridad)
                Log.e("HomeViewModel", "Excepción inesperada en generateNewSudoku", e)
                _uiState.update { it.copy(error = "Error inesperado: ${e.message}", isLoading = false, sudoku = null) }
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            val currentSudoku = _uiState.value.sudoku
            if (currentSudoku == null) {
                Log.w("HomeViewModel", "Intento de resetear sin Sudoku cargado.")
                return@launch
            }
            Log.d("HomeViewModel", "Reseteando juego...")

            try {
                val resetSudoku = resetGameUseCase(currentSudoku)
                _uiState.update {
                    it.copy(
                        sudoku = resetSudoku,
                        isGameCompleted = false,
                        selectedRow = -1,
                        selectedCol = -1,
                        error = null // Limpia errores previos al resetear
                    )
                }
                // saveSudokuProgressUseCase ya se llama dentro de resetGameUseCase
                Log.d("HomeViewModel", "Juego reseteado y guardado.")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al resetear el juego", e)
                _uiState.update { it.copy(error = "Error al resetear: ${e.message}") }
            }
        }
    }

    fun setSelectedCell(row: Int, col: Int) {
        Log.d("HomeViewModel", "Celda seleccionada: ($row, $col)")
        _uiState.update {
            it.copy(selectedRow = row, selectedCol = col)
        }
    }

    fun updateCellValue(value: Int) {
        val currentState = _uiState.value
        val sudoku = currentState.sudoku
        val row = currentState.selectedRow
        val col = currentState.selectedCol

        if (sudoku == null) {
            Log.w("HomeViewModel", "Intento de actualizar valor sin Sudoku.")
            return
        }
        if (row == -1 || col == -1) {
            Log.w("HomeViewModel", "Intento de actualizar valor sin celda seleccionada.")
            return
        }
        if (!sudoku.isEditable(row, col)) {
            Log.w("HomeViewModel", "Intento de actualizar celda no editable ($row, $col).")
            return
        }

        Log.d("HomeViewModel", "Actualizando celda ($row, $col) al valor $value")
        viewModelScope.launch {
            try {
                val updatedSudoku = sudoku.updateCell(row, col, value)
                val isGameCompleted = verifySudokuUseCase(updatedSudoku)
                saveSudokuProgressUseCase(updatedSudoku) // Guarda el progreso

                _uiState.update {
                    it.copy(
                        sudoku = updatedSudoku,
                        isGameCompleted = isGameCompleted
                        // No reseteamos la selección aquí
                    )
                }
                Log.d("HomeViewModel", "Celda actualizada y progreso guardado. Juego completo: $isGameCompleted")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al actualizar valor de celda", e)
                _uiState.update { it.copy(error = "Error al actualizar: ${e.message}") }
            }
        }
    }

    fun changeDifficulty(difficulty: Difficulty) {
        Log.d("HomeViewModel", "Cambiando dificultad a: $difficulty")
        // Actualiza la dificultad en el estado ANTES de generar
        _uiState.update { it.copy(difficulty = difficulty) }
        generateNewSudoku()
    }
}