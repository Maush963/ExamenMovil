package com.example.examen3.domain.model

data class Sudoku(
    val puzzle: List<List<Int>>,  // Representa el puzzle inicial (0 para celdas vacías)
    val currentState: List<List<Int>>,  // Estado actual del juego
    val solution: List<List<Int>>,  // Solución correcta del puzzle
    val difficulty: Difficulty,  // Nivel de dificultad
    val size: Int  // Tamaño del tablero (4 o 9)
) {
    fun isCompleted(): Boolean {
        // Comprueba si todas las celdas están llenas (no hay ceros)
        return currentState.all { row -> row.all { cell -> cell != 0 } }
    }

    fun isSolved(): Boolean {
        // Comprueba si el estado actual coincide con la solución
        return currentState == solution
    }

    fun isValidMove(row: Int, col: Int, value: Int): Boolean {
        // Comprobar si el valor coincide con la solución en esa posición
        return solution[row][col] == value
    }

    fun isOriginalCell(row: Int, col: Int): Boolean {
        // Comprobar si la celda forma parte del puzzle original
        return puzzle[row][col] != 0
    }

    // Nuevo método para compatibilidad con SudokuBoard
    fun isEditable(row: Int, col: Int): Boolean {
        // Una celda es editable si NO es parte del puzzle original
        return !isOriginalCell(row, col)
    }

    // Método para actualizar el estado actual del tablero
    fun updateCell(row: Int, col: Int, value: Int): Sudoku {
        val newState = currentState.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) value else cell
            }
        }
        return copy(currentState = newState)
    }
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}