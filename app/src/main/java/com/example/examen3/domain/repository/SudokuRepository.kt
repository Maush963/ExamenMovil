package com.example.examen3.domain.repository

import com.example.examen3.domain.model.Difficulty
import com.example.examen3.domain.model.Sudoku
import kotlinx.coroutines.flow.Flow

interface SudokuRepository {
    /**
     * Genera un nuevo puzzle de Sudoku desde la API
     * @param size Tamaño del tablero (4 o 9)
     * @param difficulty Nivel de dificultad
     * @return Flow que emite el Sudoku generado
     */
    suspend fun generateSudoku(size: Int, difficulty: Difficulty): Flow<Result<Sudoku>>

    /**
     * Guarda el progreso actual del Sudoku para jugar offline
     * @param sudoku El sudoku actual con su estado
     */
    suspend fun saveSudokuProgress(sudoku: Sudoku)

    /**
     * Carga el último Sudoku guardado
     * @return El Sudoku guardado o null si no hay ninguno
     */
    suspend fun loadSudokuProgress(): Sudoku?

    /**
     * Borra el Sudoku guardado
     */
    suspend fun clearSudokuProgress()
}