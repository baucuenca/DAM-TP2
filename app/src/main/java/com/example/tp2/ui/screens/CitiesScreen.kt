package com.example.tp2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tp2.data.local.entities.CapitalCity
import com.example.tp2.ui.views.CitiesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    citiesViewModel: CitiesViewModel = viewModel(),
    navController: NavController
) {
    val allCities by citiesViewModel.allCities.collectAsState()
    val message by citiesViewModel.message.observeAsState()
    val queriedCity by citiesViewModel.queriedCity.observeAsState()
    val showModifyDialog by citiesViewModel.showModifyDialog.observeAsState(false)
    val cityToModify by citiesViewModel.cityToModify.observeAsState()

    val countryInputText by citiesViewModel.countryInput.collectAsState()
    val cityInputText by citiesViewModel.cityInput.collectAsState()
    val populationInputText by citiesViewModel.populationInput.collectAsState()

    var queryCityName by remember { mutableStateOf("") }
    var deleteCityName by remember { mutableStateOf("") }
    var deleteByCountryName by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(message) {
        message?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                citiesViewModel.clearMessage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Capitales del Mundo") },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Añadir Ciudad Capital", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = countryInputText,
                    onValueChange = { citiesViewModel.countryInput.value = it },
                    label = { Text("Nombre del País") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = cityInputText,
                    onValueChange = { citiesViewModel.cityInput.value = it },
                    label = { Text("Nombre de la Ciudad Capital") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = populationInputText,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && (newValue.length <= 1 || newValue.first() != '0')) {
                            citiesViewModel.populationInput.value = newValue
                        }
                    },
                    label = { Text("Población Aproximada") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                Button(
                    onClick = { citiesViewModel.addCity() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Ciudad Capital")
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Consultar Ciudad", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = queryCityName,
                        onValueChange = { queryCityName = it },
                        label = { Text("Nombre de la Ciudad") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        singleLine = true
                    )
                    Button(onClick = { citiesViewModel.queryCity(queryCityName) }) {
                        Text("Consultar")
                    }
                }
                if (queriedCity != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("País: ${queriedCity?.countryName}", style = MaterialTheme.typography.bodyLarge)
                            Text("Capital: ${queriedCity?.cityName}", style = MaterialTheme.typography.bodyLarge)
                            Text("Población: ${queriedCity?.population}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                } else if (message?.contains("no encontrada") == true && queryCityName.isNotBlank()) {
                    Text("No se encontró la ciudad: $queryCityName.", modifier = Modifier.padding(top = 8.dp))
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Borrar Ciudad por Nombre", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = deleteCityName,
                        onValueChange = { deleteCityName = it },
                        label = { Text("Nombre de la Ciudad a Borrar") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        singleLine = true
                    )
                    Button(
                        onClick = { citiesViewModel.deleteCityByName(deleteCityName) }
                    ) {
                        Text("Borrar")
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Borrar Ciudades por País", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = deleteByCountryName,
                        onValueChange = { deleteByCountryName = it },
                        label = { Text("Nombre del País") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        singleLine = true
                    )
                    Button(
                        onClick = { citiesViewModel.deleteCitiesByCountry(deleteByCountryName) }
                    ) {
                        Text("Borrar Todas")
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 8.dp))

            Text("Todas las Ciudades Capitales", fontSize = 22.sp, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            if (allCities.isEmpty()) {
                Text("No hay ciudades registradas.", modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                ) {
                    items(allCities) { city ->
                        CityItem(city = city, citiesViewModel = citiesViewModel)
                    }
                }
            }
        }
    }

    if (showModifyDialog) {
        cityToModify?.let { city ->
            AlertDialog(
                onDismissRequest = { citiesViewModel.dismissModifyDialog() },
                title = { Text("Modificar Población de ${city.cityName}") },
                text = {
                    OutlinedTextField(
                        value = populationInputText,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && (newValue.length <= 1 || newValue.first() != '0')) {
                                citiesViewModel.populationInput.value = newValue
                            }
                        },
                        label = { Text("Nueva Población") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = { citiesViewModel.modifyPopulation(city, populationInputText) }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { citiesViewModel.dismissModifyDialog() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun CityItem(city: CapitalCity, citiesViewModel: CitiesViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("País: ${city.countryName}", style = MaterialTheme.typography.bodyLarge)
                Text("Capital: ${city.cityName}", style = MaterialTheme.typography.bodyLarge)
                Text("Población: ${city.population}", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                IconButton(onClick = { citiesViewModel.showModifyDialog(city) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Modificar")
                }
                IconButton(onClick = {
                    citiesViewModel.deleteCityByName(city.cityName)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCitiesScreen() {
    MaterialTheme {
        CitiesScreen(navController = rememberNavController())
    }
}