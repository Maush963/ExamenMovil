package com.example.examen3.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.examen3.data.local.model.SudokuCache
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuPreferences @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        PreferencesConstants.PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    fun saveSudokuProgress(sudokuCache: SudokuCache) {
        val sudokuJson = gson.toJson(sudokuCache)
        preferences.edit()
            .putString(PreferencesConstants.KEY_CURRENT_SUDOKU, sudokuJson)
            .apply()
    }

    fun getSudokuProgress(): SudokuCache? {
        val sudokuJson = preferences.getString(PreferencesConstants.KEY_CURRENT_SUDOKU, null)
        return if (sudokuJson != null) {
            gson.fromJson(sudokuJson, SudokuCache::class.java)
        } else {
            null
        }
    }

    fun clearSudokuProgress() {
        preferences.edit()
            .remove(PreferencesConstants.KEY_CURRENT_SUDOKU)
            .apply()
    }
}