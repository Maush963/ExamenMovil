package com.example.examen3.domain.usecase

import com.example.examen3.domain.model.Difficulty
import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(size: Int, difficulty: Difficulty): Flow<Result<Sudoku>> {
        return repository.generateSudoku(size, difficulty)
    }
}