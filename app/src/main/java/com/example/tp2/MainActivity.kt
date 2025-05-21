package com.example.tp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp2.navigation.AppRoutes
import com.example.tp2.ui.screens.CitiesScreen
import com.example.tp2.ui.screens.HomeScreen
import com.example.tp2.ui.screens.NumberGameScreen
import com.example.tp2.ui.theme.Tp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tp2Theme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.CITIES,
                        modifier = Modifier.padding(innerPadding) // Se aplica el innerPadding al NavHost
                    ) {
                        composable(AppRoutes.CITIES) {
                            CitiesScreen(navController = navController)
                        }

                        composable(AppRoutes.NUMBER_GAME) {
                            NumberGameScreen(navController = navController)
                        }
                        composable(AppRoutes.HOME) {
                            HomeScreen(navController = navController)
                       }
                    }
                }
            }
        }
    }
}