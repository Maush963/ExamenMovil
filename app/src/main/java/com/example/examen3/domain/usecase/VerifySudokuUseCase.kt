package com.example.examen3.domain.usecase

import com.example.examen3.domain.model.Sudoku
import javax.inject.Inject

class VerifySudokuUseCase @Inject constructor() {
    operator fun invoke(sudoku: Sudoku): Boolean {
        return sudoku.isSolved()
    }

    fun verifyCell(sudoku: Sudoku, row: Int, col: Int, value: Int): Boolean {
        return sudoku.isValidMove(row, col, value)
    }
}