package com.joseruiz.suprstudent.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joseruiz.suprstudent.R
import com.joseruiz.suprstudent.data.Exercise
import com.joseruiz.suprstudent.models.ExerciseViewModel

@Composable
fun ExerciseScreen() {

    val exerciseViewModel: ExerciseViewModel = viewModel()

    // Desencadena la llamada a la API al iniciar la pantalla
    exerciseViewModel.onTypeMuscle("biceps")  // Simula la llamada para mostrar los ejercicios de bíceps al iniciar

    val exerciseState = exerciseViewModel.exerciseState.value

    val viewState by exerciseViewModel.exerciseState

    // Comienza la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Estado de carga o error
        when {

            viewState.loading -> {
                CircularProgressIndicator()
            }
            viewState.error != null -> {
                Text("ERROR OCCURRED RECIPE: ${viewState.error}")
            }
            viewState.list.isNullOrEmpty() -> { // Manejo de lista nula o vacía
                Text("No recipes found.")
            }
            else -> {
                // Muestra la lista de ejercicios
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