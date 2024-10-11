package com.joseruiz.suprstudent.repository

import android.content.Context
import android.util.Log
import com.joseruiz.suprstudent.api.ApiService
import com.joseruiz.suprstudent.api.checkForInternet
import com.joseruiz.suprstudent.dao.ExerciseDao
import com.joseruiz.suprstudent.data.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExerciseRepository (
    private val dao: Any,
    private val apiService: ApiService,
    private val context: Context
) {
    val exerciseDao = dao as ExerciseDao

    fun getExercisesMuscle(muscleName: String): Flow<List<Exercise>> = flow {
        if (checkForInternet(context)) {
            Log.i("SI hay internet", "Si hay internet")
            val musclesFromApi = apiService.getMuscles(muscleName)

            val ExerciseWithMuscles = musclesFromApi.map { muscle ->
                Exercise(
                    name = muscle.name,
                    type = muscle.type,
                     muscle = muscleName,
                     equipment = muscle.equipment,
                     difficulty = muscle.difficulty,
                     instructions = muscle.instructions
                )
            }
            exerciseDao.insertExercise(ExerciseWithMuscles)
            Log.i("Se insertó correctamente", "Se insertó correctamente")

            // Emitir la lista de categorías obtenida de la API
            emit(musclesFromApi)
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            exerciseDao.getAllMuscles(muscleName).collect { musclesFromDb ->
                emit(musclesFromDb)
            }
        }
    }

    fun getExercisesType(typeName: String): Flow<List<Exercise>> = flow {
        if (checkForInternet(context)) {
            Log.i("SI hay internet", "Si hay internet")
            val typesFromApi = apiService.getTypes(typeName)

            val ExerciseWithMuscles = typesFromApi.map { exercise ->
                Exercise(
                    name = exercise.name,
                    type = typeName,
                    muscle = exercise.muscle,
                    equipment = exercise.equipment,
                    difficulty = exercise.difficulty,
                    instructions = exercise.instructions
                )
            }
            exerciseDao.insertExercise(ExerciseWithMuscles)
            Log.i("Se insertó correctamente", "Se insertó correctamente")

            // Emitir la lista de categorías obtenida de la API
            emit(typesFromApi)
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            exerciseDao.getAllTypes(typeName).collect { musclesFromDb ->
                emit(musclesFromDb)
            }
        }
    }

    fun getExercisesDifficulty(difficultyName: String): Flow<List<Exercise>> = flow {
        if (checkForInternet(context)) {
            Log.i("SI hay internet", "Si hay internet")
            val difficultyFromApi = apiService.getDifficulty(difficultyName)

            val ExerciseWithMuscles = difficultyFromApi.map { exercise ->
                Exercise(
                    name = exercise.name,
                    type = exercise.type,
                    muscle = exercise.muscle,
                    equipment = exercise.equipment,
                    difficulty = difficultyName,
                    instructions = exercise.instructions
                )
            }
            exerciseDao.insertExercise(ExerciseWithMuscles)
            Log.i("Se insertó correctamente", "Se insertó correctamente")

            // Emitir la lista de categorías obtenida de la API
            emit(difficultyFromApi)
        } else {
            Log.i("No hay internet", "no hay internet")
            // Si no hay internet, obtener de la base de datos local
            exerciseDao.getAllDifficulty(difficultyName).collect { musclesFromDb ->
                emit(musclesFromDb)
            }
        }
    }
}