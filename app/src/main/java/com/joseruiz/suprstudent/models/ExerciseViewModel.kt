package com.joseruiz.suprstudent.models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseruiz.suprstudent.api.ApiService
import com.joseruiz.suprstudent.api.exerciseService
import com.joseruiz.suprstudent.data.Exercise
import com.joseruiz.suprstudent.repository.ExerciseRepository

import kotlinx.coroutines.launch

class ExerciseViewModel(
    val dao: Any,
    val apiService: ApiService,
    val context: Context,
    val parametro: String
) : ViewModel() {

    private val _exerciseState = mutableStateOf(ExerciseState())
    val exerciseState: State<ExerciseState> = _exerciseState

    private var localExerciseList: List<Exercise> = emptyList()

    private var selectedMuscle: String = "Sin seleccion"
    private var selectedType: String = "Sin seleccion"
    private var selectedDifficulty: String = "Sin seleccion"

    private val exerciseRepository = ExerciseRepository(dao, apiService, context)

    /*************************MUSCLES********************************/
    private fun fetchMuscle(parametro: String) {
        viewModelScope.launch {
            exerciseRepository.getExercisesMuscle(parametro).collect { muscles ->
                try {
                    localExerciseList = muscles // Almacena la lista filtrada por músculo
                    filterExercises() // Filtra inmediatamente después de obtener los músculos
                } catch (e: Exception) {
                    _exerciseState.value = _exerciseState.value.copy(
                        loading = false,
                        error = "Error fetching exercises: ${e.message}"
                    )
                }
            }
        }
    }

    fun onTypeMuscle(muscle: String) {
        selectedMuscle = muscle
        fetchMuscle(muscle)
    }

    /*************************FILTERING********************************/
    fun onTypeType(type: String) {
        selectedType = type
        filterExercises()
    }

    fun onTypeDifficulty(difficulty: String) {
        selectedDifficulty = difficulty
        filterExercises()
    }

    private fun filterExercises() {
        var filteredList = localExerciseList

        // Aplica el filtro de músculo
        if (selectedMuscle.isNotBlank() && selectedMuscle != "Sin seleccion") {
            filteredList = filteredList.filter { it.muscle == selectedMuscle }
        }

        // Aplica el filtro de tipo
        if (selectedType != "Sin seleccion") {
            filteredList = filteredList.filter { it.type == selectedType }
        }

        // Aplica el filtro de dificultad
        if (selectedDifficulty != "Sin seleccion") {
            filteredList = filteredList.filter { it.difficulty == selectedDifficulty }
        }

        // Verifica si la lista filtrada está vacía
        if (filteredList.isEmpty()) {
            _exerciseState.value = _exerciseState.value.copy(
                list = filteredList,
                loading = false,
                error = "No existen ejercicios con los filtros solicitados" // Actualiza el mensaje de error si no hay ejercicios
            )
        } else {
            // Actualiza el estado con la lista filtrada
            _exerciseState.value = _exerciseState.value.copy(
                list = filteredList,
                loading = false,
                error = null // Reinicia el mensaje de error si hay ejercicios
            )
        }
    }

    data class ExerciseState(
        val loading: Boolean = true,
        val list: List<Exercise> = emptyList(),
        val error: String? = null
    )
}
