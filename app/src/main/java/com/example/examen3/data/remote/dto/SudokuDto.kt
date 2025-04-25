package com.example.examen3.data.remote.dto

import com.google.gson.annotations.SerializedName // Importa SerializedName

data class SudokuDto(
    // Usa @SerializedName para mapear desde "puzzle" en el JSON
    @SerializedName("puzzle")
    val puzzle: List<List<Int?>>, // Cambia a List<List<Int?>> para coincidir con el JSON

    // Usa @SerializedName para mapear desde "solution" en el JSON
    @SerializedName("solution")
    val solution: List<List<Int?>>, // Cambia a List<List<Int?>>

    // Asumiendo que estos SÍ vienen de la API, aunque no estén en tu ejemplo JSON
    // Si no vienen, tendrás que obtenerlos de otra forma o quitarlos.
    // Si los nombres en la API son diferentes, usa @SerializedName también.
    @SerializedName("difficulty") // <- Revisa si este es el nombre correcto en la API
    val difficultyLevel: String?, // Hazlo nullable por si acaso

    @SerializedName("size") // <- Revisa si este es el nombre correcto en la API
    val gridSize: Int? // Hazlo nullable por si acaso
)