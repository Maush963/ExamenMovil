package com.example.examen3.data.local.model

data class SudokuCache(
    val puzzle: List<List<Int>>,      // Puzzle original con celdas vacías (0)
    val currentState: List<List<Int>>, // Estado actual del tablero con entradas del usuario
    val solution: List<List<Int>>,     // Solución completa
    val difficulty: String,            // "easy", "medium", "hard"
    val size: Int,                     // 4 o 9 (tamaño del tablero)
    val timestamp: Long                // Cuando se guardó
)