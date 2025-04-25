package com.example.examen3.data.mapper

import com.example.examen3.data.local.model.SudokuCache
import com.example.examen3.data.remote.dto.SudokuDto
import com.example.examen3.domain.model.Difficulty
import com.example.examen3.domain.model.Sudoku
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuMapper @Inject constructor() {

    fun dtoToDomain(dto: SudokuDto): Sudoku {
        // --- CORRECCIÓN: Mapear directamente desde List<List<Int?>> ---

        // Convierte la lista de listas de Int? a lista de listas de Int, reemplazando null con 0
        val puzzleList: List<List<Int>> = dto.puzzle?.map { row ->
            row.map { cell -> cell ?: 0 } // Si cell es null, usa 0
        } ?: emptyList() // Si dto.puzzle es null, usa lista vacía

        // Convierte la solución de la misma manera (aunque aquí podríamos mantener nulls si quisiéramos, pero Int es más simple si no hay ceros en la solución)
        val solutionList: List<List<Int>> = dto.solution?.map { row ->
            row.mapNotNull { cell -> cell } // Mantenemos solo los números, quitamos nulls (o podrías poner 0 si prefieres)
        } ?: emptyList() // Si dto.solution es null, usa lista vacía

        // --- Mismo manejo de dificultad y tamaño que antes ---
        val difficultyEnum = when(dto.difficultyLevel?.lowercase()) {
            "easy" -> Difficulty.EASY
            "medium" -> Difficulty.MEDIUM
            "hard" -> Difficulty.HARD
            else -> Difficulty.MEDIUM // valor por defecto si es nulo o no coincide
        }

        // Usa el tamaño del puzzle recibido o un valor por defecto
        val gridSize = puzzleList.firstOrNull()?.size ?: dto.gridSize ?: 9 // Intenta obtener tamaño de la lista, luego del DTO, o usa 9

        // Crea el estado inicial mutable a partir del puzzleList
        val initialState = puzzleList.map { row -> row.toMutableList() }.toMutableList()

        return Sudoku(
            puzzle = puzzleList, // El puzzle inmutable (con 0s para vacíos)
            currentState = initialState, // El estado mutable para jugar
            solution = solutionList, // La solución
            difficulty = difficultyEnum,
            size = gridSize // El tamaño calculado
        )
    }

    // --- Las funciones domainToCache y cacheToDomain permanecen igual ---
    fun domainToCache(sudoku: Sudoku): SudokuCache {
        val difficultyString = when(sudoku.difficulty) {
            Difficulty.EASY -> "easy"
            Difficulty.MEDIUM -> "medium"
            Difficulty.HARD -> "hard"
        }
        return SudokuCache(
            puzzle = sudoku.puzzle,
            currentState = sudoku.currentState,
            solution = sudoku.solution,
            difficulty = difficultyString,
            size = sudoku.size,
            timestamp = System.currentTimeMillis()
        )
    }

    fun cacheToDomain(cache: SudokuCache): Sudoku {
        val difficultyEnum = when(cache.difficulty.lowercase()) {
            "easy" -> Difficulty.EASY
            "medium" -> Difficulty.MEDIUM
            "hard" -> Difficulty.HARD
            else -> Difficulty.MEDIUM
        }
        val currentState = cache.currentState ?: cache.puzzle.map { it.toMutableList() }.toMutableList()
        return Sudoku(
            puzzle = cache.puzzle,
            currentState = currentState,
            solution = cache.solution,
            difficulty = difficultyEnum,
            size = cache.size
        )
    }
}