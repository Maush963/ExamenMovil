package com.example.examen3.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen3.R // Asegúrate que este import sea correcto
import com.example.examen3.presentation.screens.home.HomeScreen

sealed class Screen(val route: String) { // Simplificado: ya no necesita title ni icon
    object Home : Screen(route = "home")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    // Ya no se necesita la lógica de screens, currentRoute para la bottom bar

    Scaffold{ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding) // El padding sigue siendo necesario por el Scaffold
        ) {
            composable(Screen.Home.route) {
                HomeScreen() // Asume que HomeScreen recibe el ViewModel internamente vía Hilt
            }
            // Puedes añadir otras pantallas aquí si las necesitas en el futuro
        }
    }
}