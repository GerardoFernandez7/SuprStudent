package com.joseruiz.suprstudent.models

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseruiz.suprstudent.api.ApiService
import com.joseruiz.suprstudent.api.exerciseService
import com.joseruiz.suprstudent.data.Exercise

import kotlinx.coroutines.launch


class ExerciseViewModel : ViewModel() {

    // Variables para ejercicios
    private val _exerciseState = mutableStateOf(ExerciseState())
    val exerciseState: State<ExerciseState> = _exerciseState

    /*************************MUSCLES********************************/
    private fun fetchMuscle(muscle: String) {
        viewModelScope.launch {
            Log.d("ExerciseViewModel", "Fetching exercises for muscle: $muscle") // Log para depuración
            try {
                val response = exerciseService.getMuscles(muscle)  // Aquí ya obtenemos directamente la lista
                Log.d("ExerciseViewModel", "Received response: $response") // Log para depuración
                _exerciseState.value = _exerciseState.value.copy(
                    list = response,  // Asigna directamente la lista a 'list'
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("ExerciseViewModel", "Error fetching exercises: ${e.message}") // Log de error
                _exerciseState.value = _exerciseState.value.copy(
                    loading = false,
                    error = "Error fetching exercises: ${e.message}"
                )
            }
        }
    }

    fun onTypeMuscle(muscle: String) {
        fetchMuscle(muscle)
    }

    data class ExerciseState(
        val loading: Boolean = true,
        val list: List<Exercise> = emptyList(),
        val error: String? = null
    )

    /*************************TYPES********************************/

    private fun fetchType(type: String) {
        viewModelScope.launch {
            try {
                val response = exerciseService.getTypes(type)  // Aquí ya obtenemos directamente la lista
                Log.d("ExerciseViewModel", "Received response: $response")
                _exerciseState.value = _exerciseState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _exerciseState.value = _exerciseState.value.copy(
                    loading = false,
                    error = "Error fetching exercises: ${e.message}"
                )
            }
        }
    }

    fun onTypeType(type: String) {
        fetchType(type)
    }

    /*************************difficulty********************************/
    private fun fetchDifficulty(difficulty: String) {
        viewModelScope.launch {
            try {
                val response = exerciseService.getDifficulty(difficulty)  // Aquí ya obtenemos directamente la lista
                Log.d("ExerciseViewModel", "Received response: $response")
                _exerciseState.value = _exerciseState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _exerciseState.value = _exerciseState.value.copy(
                    loading = false,
                    error = "Error fetching exercises: ${e.message}"
                )
            }
        }
    }

    fun onTypeDifficulty(difficulty: String) {
        fetchDifficulty(difficulty)
    }

}
