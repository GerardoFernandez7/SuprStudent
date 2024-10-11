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

    // Variables para ejercicios
    private val _exerciseState = mutableStateOf(ExerciseState())
    val exerciseState: State<ExerciseState> = _exerciseState

    //LLamada al repositorio
    private val exerciseRepository = ExerciseRepository(dao, apiService, context)

    /*************************MUSCLES********************************/
    private fun fetchMuscle(parametro: String) {
        viewModelScope.launch {
            exerciseRepository.getExercisesMuscle(parametro).collect { muscles ->
                try {
                    _exerciseState.value = _exerciseState.value.copy(
                        list = muscles,
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

    private fun fetchType(parametro: String) {
        viewModelScope.launch {
            exerciseRepository.getExercisesType(parametro).collect { exercises ->
                try {
                    _exerciseState.value = _exerciseState.value.copy(
                        list = exercises,
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
    }

    fun onTypeType(type: String) {
        fetchType(type)
    }

    /*************************difficulty********************************/
    private fun fetchDifficulty(parametro: String) {
        viewModelScope.launch {
            exerciseRepository.getExercisesDifficulty(parametro).collect { exercises ->
                try {
                    _exerciseState.value = _exerciseState.value.copy(
                        list = exercises,
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
    }

    fun onTypeDifficulty(difficulty: String) {
        fetchDifficulty(difficulty)
    }

}
