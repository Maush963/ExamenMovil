package com.example.examen3.domain.usecase

import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.repository.SudokuRepository
import javax.inject.Inject

class ResetGameUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(sudoku: Sudoku): Sudoku {
        // Reinicia el estado actual al estado inicial del puzzle
        val resetSudoku = sudoku.copy(
            currentState = sudoku.puzzle.map { row -> row.toMutableList() }.toMutableList()
        )

        // Guarda el progreso reiniciado
        repository.saveSudokuProgress(resetSudoku)

        return resetSudoku
    }
}