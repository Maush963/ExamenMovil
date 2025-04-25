package com.example.examen3.data.remote.api

import com.example.examen3.data.remote.dto.SudokuDto
import retrofit2.Response // Asegúrate de importar Response de Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface SudokuApi {
    @GET("sudokugenerate")
    suspend fun generateSudoku(
        @Query("difficulty") difficulty: String,
        @Query("width") width: Int, // Ancho de la caja (ej. 3 para 9x9)
        @Query("height") height: Int // Alto de la caja (ej. 3 para 9x9)
    ): Response<SudokuDto> // <-- Debe devolver Response<SudokuDto>
}