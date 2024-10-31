package com.joseruiz.suprstudent.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joseruiz.suprstudent.R
import com.joseruiz.suprstudent.api.exerciseService
import com.joseruiz.suprstudent.dao.ExerciseDao
import com.joseruiz.suprstudent.data.AppDatabase
import com.joseruiz.suprstudent.data.Exercise
import com.joseruiz.suprstudent.models.ExerciseViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(navController: NavController) {
    val context = LocalContext.current
    val exerciseDao: ExerciseDao = AppDatabase.getDatabase(context).exerciseDao()
    val apiService = exerciseService

    val exerciseViewModel: ExerciseViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ExerciseViewModel(exerciseDao, apiService, context, "") as T
            }
        }
    )

    // Estado para controlar el filtro actualmente activo
    var activeFilter by remember { mutableStateOf("muscle") }

    // Lista de opciones
    val muscles = listOf(
        "Sin selección", "abdominals", "abductors", "adductors", "biceps", "calves", "chest", "forearms",
        "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps",
        "traps", "triceps"
    )

    val types = listOf("Sin selección", "cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength", "stretching", "strongman")

    val difficulties = listOf("Sin selección", "beginner", "intermediate", "expert")

    // Estado de selección de los menús
    var selectedMuscle by remember { mutableStateOf(muscles[0]) }
    var muscleExpanded by remember { mutableStateOf(false) }

    var selectedType by remember { mutableStateOf(types[0]) }
    var typeExpanded by remember { mutableStateOf(false) }

    var selectedDifficulty by remember { mutableStateOf(difficulties[0]) }
    var difficultyExpanded by remember { mutableStateOf(false) }

    // Llama al ViewModel para cargar ejercicios cuando se abre la vista por primera vez
    LaunchedEffect(Unit) {
        exerciseViewModel.onTypeMuscle(selectedMuscle)  // Carga ejercicios por defecto con muscle
    }

    // Comienza la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.align(Alignment.Start) // Alinea el botón en la esquina superior izquierda
        ) {
            Text(
                text = "< Atrás",
                color = Color(0xFF007AFF),
                fontSize = 20.sp // Ajusta el tamaño de la fuente
            )
        }

        val backgroundColor = Color(ContextCompat.getColor(context, R.color.customMaroon))

        // Menu de Muscles
        ExposedDropdownMenuBox(
            expanded = muscleExpanded,
            onExpandedChange = { muscleExpanded = !muscleExpanded }
        ) {
            OutlinedTextField(
                value = selectedMuscle,
                onValueChange = {},
                readOnly = true,
                label = { Text("Muscle") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = muscleExpanded)
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White
                    )
                },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = backgroundColor,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(
                expanded = muscleExpanded,
                onDismissRequest = { muscleExpanded = false }
            ) {
                muscles.forEach { muscle ->
                    DropdownMenuItem(onClick = {
                        selectedMuscle = muscle
                        muscleExpanded = false
                        activeFilter = "muscle"
                        exerciseViewModel.onTypeMuscle(muscle)
                        selectedType = types[0]
                        selectedDifficulty = difficulties[0]
                    }, text = { Text(muscle) })
                }
            }
        }

        // Menu de Type
        ExposedDropdownMenuBox(
            expanded = typeExpanded,
            onExpandedChange = { typeExpanded = !typeExpanded }
        ) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White
                    )
                },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = backgroundColor,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(onClick = {
                        selectedType = type
                        typeExpanded = false
                        exerciseViewModel.onTypeType(type) // Filtra por tipo
                    }, text = { Text(type) })
                }
            }
        }

        // Menu de Difficulty
        ExposedDropdownMenuBox(
            expanded = difficultyExpanded,
            onExpandedChange = { difficultyExpanded = !difficultyExpanded }
        ) {
            OutlinedTextField(
                value = selectedDifficulty,
                onValueChange = {},
                readOnly = true,
                label = { Text("Difficulty") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded)
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White
                    )},
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = backgroundColor,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(
                expanded = difficultyExpanded,
                onDismissRequest = { difficultyExpanded = false }
            ) {
                difficulties.forEach { difficulty ->
                    DropdownMenuItem(onClick = {
                        selectedDifficulty = difficulty
                        difficultyExpanded = false
                        exerciseViewModel.onTypeDifficulty(difficulty) // Filtra por dificultad
                    }, text = { Text(difficulty) })
                }
            }
        }

        // Estado de carga o error
        val exerciseState = exerciseViewModel.exerciseState.value

        when {
            exerciseState.loading && exerciseState.list.isNullOrEmpty() -> {
                CircularProgressIndicator()  // Solo muestra el loading si la lista está vacía y aún está cargando
            }
            exerciseState.error != null -> {
                Text("ERROR OCCURRED: ${exerciseState.error}")
            }
            exerciseState.list.isNullOrEmpty() -> {
                Text("No exercises found.")
            }
            else -> {
                // Muestra la lista de ejercicios filtrados
                LazyColumn {
                    items(exerciseState.list) { exercise ->
                        ExerciseItem(exercise) // Llama a un Composable para mostrar cada ejercicio
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${exercise.name}", fontWeight = FontWeight.Bold)
            Text(text = "Tipo: ${exercise.type}")
            Text(text = "Músculo: ${exercise.muscle}")
            Text(text = "Equipo: ${exercise.equipment}")
            Text(text = "Dificultad: ${exercise.difficulty}")
            Text(text = "Instrucciones: ${exercise.instructions}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview() {
    ExerciseScreen(navController = rememberNavController())
}