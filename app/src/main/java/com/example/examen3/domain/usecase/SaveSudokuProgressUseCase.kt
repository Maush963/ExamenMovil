package com.example.examen3.domain.usecase

import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.repository.SudokuRepository
import javax.inject.Inject

class SaveSudokuProgressUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(sudoku: Sudoku) {
        repository.saveSudokuProgress(sudoku)
    }
}