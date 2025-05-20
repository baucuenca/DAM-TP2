package com.example.tp2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Clase de datos para representar un resultado de intento
data class Intento(val numeroIngresado: String, val esAcierto: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGameScreen(navController: NavController) {
    // Estados para los elementos de la UI
    var numeroUsuario by remember { mutableStateOf("") }
    val puntuacionActual by remember { mutableIntStateOf(0) } // Solo para mostrar, no se actualiza
    val puntuacionMaxima by remember { mutableIntStateOf(0) } // Solo para mostrar, no se actualiza
    val resultadosIntentos = remember { mutableStateListOf<Intento>() }

    // El número "grande" en el centro, aquí es solo un marcador de posición
    val numeroAdivinarVisible by remember { mutableIntStateOf(5) } // Solo para visualización en el front-end

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Puntuación Actual y Puntuación Máxima
            Text(
                text = "Puntuación Actual: $puntuacionActual",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Puntuación Máxima: $puntuacionMaxima",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = TextAlign.Center
            )

            // Número grande en el centro (aquí solo un marcador de posición)
            Text(
                text = "$numeroAdivinarVisible", // Este sería el número a adivinar (ej: 5)
                fontSize = 100.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Input para el usuario
            OutlinedTextField(
                value = numeroUsuario,
                onValueChange = { newValue ->
                    // Permite solo números y limita a un dígito (del 1 al 5)
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        numeroUsuario = newValue
                    }
                },
                label = { Text("Ingresa un número (1-5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Ancho del input
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

            // Botón de ejemplo para "Adivinar" (sin lógica de back-end)
            Button(
                onClick = {
                    // Simulación de un intento para mostrar los resultados
                    // En una implementación real, aquí se llamaría a la lógica de adivinanza
                    val aciertoSimulado = (numeroUsuario == "3") // Simulación: acierta si ingresa '3'
                    resultadosIntentos.add(0, Intento(numeroUsuario, aciertoSimulado))
                    numeroUsuario = "" // Limpiar el input después del intento
                },
                enabled = numeroUsuario.isNotBlank(), // Habilitar el botón si hay algo en el input
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Adivinar")
            }

            // Registro de resultados
            Text(
                text = "Resultados:",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(resultadosIntentos) { intento ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Número ingresado: ${intento.numeroIngresado}",
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (intento.esAcierto) "¡Adivinaste!" else "Fallaste",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (intento.esAcierto) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberGameScreenPreview() {
    MaterialTheme {
        NumberGameScreen(navController = rememberNavController())
    }
}