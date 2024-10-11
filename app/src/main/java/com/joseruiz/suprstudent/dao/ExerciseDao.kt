package com.joseruiz.suprstudent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joseruiz.suprstudent.data.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(recipes: List<Exercise>)

    @Query("SELECT * FROM Exercise WHERE muscle = :muscle")
    fun getAllMuscles(muscle: String): Flow<List<Exercise>>

    @Query("SELECT * FROM Exercise WHERE type = :type")
    fun getAllTypes(type: String): Flow<List<Exercise>>

    @Query("SELECT * FROM Exercise WHERE difficulty = :difficulty")
    fun getAllDifficulty(difficulty: String): Flow<List<Exercise>>

}