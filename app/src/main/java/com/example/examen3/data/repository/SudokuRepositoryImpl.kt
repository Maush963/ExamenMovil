package com.example.examen3.data.repository

import com.example.examen3.data.local.preferences.SudokuPreferences
import com.example.examen3.data.mapper.SudokuMapper
import com.example.examen3.data.remote.api.SudokuApi
import com.example.examen3.domain.model.Difficulty
import com.example.examen3.domain.model.Sudoku
import com.example.examen3.domain.repository.SudokuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException // Importa HttpException para errores HTTP
import java.io.IOException // Importa IOException para errores de red
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class SudokuRepositoryImpl @Inject constructor(
    private val api: SudokuApi,
    private val preferences: SudokuPreferences,
    private val mapper: SudokuMapper
) : SudokuRepository {

    override suspend fun generateSudoku(size: Int, difficulty: Difficulty): Flow<Result<Sudoku>> = flow {
        // --- LÍNEA ELIMINADA --- No emitimos un estado vacío inicial aquí
        try {
            val difficultyString = when(difficulty) {
                Difficulty.EASY -> "easy"
                Difficulty.MEDIUM -> "medium"
                Difficulty.HARD -> "hard"
            }

            val boxSize = sqrt(size.toDouble()).toInt()
            if (boxSize * boxSize != size) {
                throw IllegalArgumentException("El tamaño del tablero debe ser un cuadrado perfecto (ej. 4, 9).")
            }
            val boxWidth = boxSize
            val boxHeight = boxSize

            val response = api.generateSudoku(difficultyString, boxWidth, boxHeight)

            if (response.isSuccessful) {
                val sudokuDto = response.body()
                if (sudokuDto != null) {
                    val sudoku = mapper.dtoToDomain(sudokuDto)
                    // Guarda en preferencias ANTES de emitir el éxito
                    val sudokuCache = mapper.domainToCache(sudoku)
                    preferences.saveSudokuProgress(sudokuCache)
                    // Emite el resultado exitoso
                    emit(Result.success(sudoku))
                } else {
                    throw Exception("La respuesta de la API está vacía.")
                }
            } else {
                // Lanza una excepción específica para errores HTTP
                throw HttpException(response)
            }

        } catch (e: HttpException) {
            // Error específico de HTTP (ej. 404, 500)
            e.printStackTrace()
            // Intenta cargar desde caché o propaga el error
            emit(loadFromCacheOrPropagateError(e))
        } catch (e: IOException) {
            // Error de red (sin conexión, timeout)
            e.printStackTrace()
            // Intenta cargar desde caché o propaga el error
            emit(loadFromCacheOrPropagateError(e))
        } catch (e: Exception) {
            // Otros errores (ej. IllegalArgumentException, error de mapeo)
            e.printStackTrace()
            // Intenta cargar desde caché o propaga el error
            emit(loadFromCacheOrPropagateError(e))
        }
    }.flowOn(Dispatchers.IO) // Asegura que toda la lógica se ejecute en el hilo IO

    // Función helper para cargar desde caché o devolver el error original
    private suspend fun loadFromCacheOrPropagateError(originalError: Exception): Result<Sudoku> {
        return try {
            val cachedProgress = preferences.getSudokuProgress()
            if (cachedProgress != null) {
                Result.success(mapper.cacheToDomain(cachedProgress))
            } else {
                // No hay caché, propaga el error original que causó el catch
                Result.failure(originalError)
            }
        } catch (cacheException: Exception) {
            // Si falla la lectura de caché, también propaga el error original
            cacheException.printStackTrace() // Log del error de caché
            Result.failure(originalError)
        }
    }


    override suspend fun saveSudokuProgress(sudoku: Sudoku) {
        try {
            val sudokuCache = mapper.domainToCache(sudoku)
            preferences.saveSudokuProgress(sudokuCache)
        } catch (e: Exception) {
            e.printStackTrace() // Manejar error al guardar si es necesario
        }
    }

    override suspend fun loadSudokuProgress(): Sudoku? {
        return try {
            preferences.getSudokuProgress()?.let { mapper.cacheToDomain(it) }
        } catch (e: Exception) {
            e.printStackTrace() // Manejar error al cargar si es necesario
            null
        }
    }

    override suspend fun clearSudokuProgress() {
        try {
            preferences.clearSudokuProgress()
        } catch (e: Exception) {
            e.printStackTrace() // Manejar error al limpiar si es necesario
        }
    }
}