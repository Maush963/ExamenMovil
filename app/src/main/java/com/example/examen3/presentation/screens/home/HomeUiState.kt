package com.example.examen3.presentation.screens.home

import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.model.Difficulty

data class HomeUiState(
    val sudoku: Sudoku? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedRow: Int = -1,
    val selectedCol: Int = -1,
    val isGameCompleted: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
    val boardSize: Int = 9
)