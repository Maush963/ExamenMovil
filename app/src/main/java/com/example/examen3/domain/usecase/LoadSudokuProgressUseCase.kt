package com.example.examen3.domain.usecase

import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.repository.SudokuRepository
import javax.inject.Inject

class LoadSudokuProgressUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(): Sudoku? {
        return repository.loadSudokuProgress()
    }
}