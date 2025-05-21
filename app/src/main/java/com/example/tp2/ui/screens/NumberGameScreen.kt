// com.example.tp2.ui.screens.NumberGameScreen.kt
package com.example.tp2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tp2.ui.views.NumberGameViewModel
import kotlinx.coroutines.launch

data class Intento(val numeroIngresado: String, val numeroCorrecto: Int, val esAcierto: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberGameScreen(
    navController: NavController
) {
    val numberGameViewModel: NumberGameViewModel = viewModel()

    var numeroUsuario by remember { mutableStateOf("") }
    val puntuacionActual by numberGameViewModel.currentScore
    val puntuacionMaxima by numberGameViewModel.maxScore

    val resultadosIntentos = remember { mutableStateListOf<Intento>() }
    var numeroAdivinar by remember { mutableIntStateOf((1..5).random()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Juego de Adivinar el Número") },
                navigationIcon = {
                    TextButton(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(32.dp))

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

            OutlinedTextField(
                value = numeroUsuario,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && newValue.all { it.isDigit() } && newValue.toIntOrNull() in 1..5) {
                        numeroUsuario = newValue
                    } else if (newValue.isEmpty()) {
                        numeroUsuario = newValue
                    }
                },
                label = { Text("Ingresa un número (1-5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 16.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

            Button(
                onClick = {
                    val userGuess = numeroUsuario.toIntOrNull()
                    if (userGuess == null || userGuess !in 1..5) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Por favor, ingresa un número válido entre 1 y 5.",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }

                    val acierto = userGuess == numeroAdivinar
                    resultadosIntentos.add(0, Intento(numeroUsuario, numeroAdivinar, acierto))

                    numberGameViewModel.atemp(acierto)

                    if (acierto) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "¡Adivinaste el número $numeroAdivinar!",
                                duration = SnackbarDuration.Long
                            )
                        }
                        numeroAdivinar = (1..5).random()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Fallaste. El número era $numeroAdivinar. Intenta de nuevo.",
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                    numeroUsuario = ""
                },
                enabled = numeroUsuario.isNotBlank(),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Adivinar")
            }

            Text(
                text = "Intentos Anteriores:",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (resultadosIntentos.isEmpty()) {
                Text("No hay intentos aún.", modifier = Modifier.padding(vertical = 8.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
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
                                    text = "Tu número: ${intento.numeroIngresado}",
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "Número correcto: ${intento.numeroCorrecto}",
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
}

@Preview(showBackground = true)
@Composable
fun PreviewNumberGameScreen() {
    MaterialTheme {
        NumberGameScreen(navController = rememberNavController())
    }
}