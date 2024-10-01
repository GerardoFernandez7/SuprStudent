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
import com.joseruiz.suprstudent.R
import com.joseruiz.suprstudent.models.Exercise

fun getExercises(): List<Exercise> {
    return listOf(
        Exercise(
            "Incline Hammer Curls", "strength", "biceps", "dumbbell", "beginner",
            "Seat yourself on an incline bench with a dumbbell in each hand. Curl the weights while keeping your elbows close to your body."
        ),
        Exercise(
            "Wide-grip Barbell Curl", "strength", "biceps", "barbell", "beginner",
            "Stand up with your torso upright while holding a barbell with a wide grip. Curl the barbell towards your shoulders."
        ),
        Exercise(
            "Flat Bench Press", "strength", "chest", "barbell", "intermediate",
            "Lie on a flat bench and grasp the barbell with a shoulder-width grip. Lower the barbell to your chest and then press it back up."
        ),
        Exercise(
            "Squats", "strength", "quadriceps", "bodyweight", "beginner",
            "Stand with your feet shoulder-width apart and lower your body by bending your knees, keeping your back straight."
        ),
        Exercise(
            "Deadlifts", "strength", "hamstrings", "barbell", "expert",
            "Stand with your feet hip-width apart and grasp a barbell. Keep your back straight and lift the barbell by straightening your hips and knees."
        ),
        Exercise(
            "Push-ups", "strength", "chest", "bodyweight", "beginner",
            "Start in a plank position and lower your body until your chest nearly touches the floor, then push back up."
        ),
        Exercise(
            "Plank", "strength", "core", "bodyweight", "beginner",
            "Get into a push-up position and hold your body in a straight line from head to heels for as long as you can."
        ),
        Exercise(
            "Lunges", "strength", "quadriceps", "bodyweight", "intermediate",
            "Stand tall, take a step forward with one leg, lower your hips until both knees are bent at about a 90-degree angle, then push back to the start."
        ),
        Exercise(
            "Lat Pulldown", "strength", "back", "machine", "intermediate",
            "Sit at a lat pulldown machine and pull the bar down to your chest while keeping your back straight."
        ),
        Exercise(
            "Dumbbell Shoulder Press", "strength", "shoulders", "dumbbell", "beginner",
            "Sit or stand with a dumbbell in each hand. Press the weights overhead until your arms are fully extended."
        ),
        Exercise(
            "Burpees", "cardio", "full body", "bodyweight", "intermediate",
            "Start in a standing position, drop into a squat, kick your feet back to a plank position, return to squat, and jump up."
        ),
        Exercise(
            "Jump Rope", "cardio", "full body", "equipment", "beginner",
            "Use a jump rope to perform continuous jumps, which improves cardiovascular fitness."
        ),
        Exercise(
            "Leg Press", "strength", "quadriceps", "machine", "intermediate",
            "Sit on a leg press machine and push the platform away using your legs."
        ),
        Exercise(
            "Bench Dips", "strength", "triceps", "bodyweight", "beginner",
            "Sit on a bench with your hands beside you, lower your body towards the ground, then push back up."
        ),
        Exercise(
            "Russian Twists", "strength", "core", "bodyweight", "intermediate",
            "Sit on the ground with your knees bent, lean back slightly and twist your torso side to side."
        ),
        Exercise(
            "Box Jumps", "strength", "legs", "bodyweight", "expert",
            "Stand in front of a sturdy box or platform and jump onto it, landing softly."
        ),
        Exercise(
            "Kettlebell Swings", "strength", "full body", "kettlebell", "intermediate",
            "Stand with your feet shoulder-width apart, swing a kettlebell between your legs and then up to shoulder height."
        )
    )
}

@Preview(
    showBackground = true
)
@Composable
fun ExerciseScreen() {
    val exercises = getExercises()
    var filteredExercises by remember { mutableStateOf(exercises) }

    Column {
        TopBarExercise()
        ExerciseFilterView(exercises = exercises, onFilterChange = {
            filteredExercises = it
        })
        ExerciseListView(exercises = filteredExercises)
    }
}

@Composable
fun TopBarExercise() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Transparent)
    ) {
        // Encabezado
        Image(
            painter = painterResource(id = R.drawable.top),
            contentDescription = "Top",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
        )

        // Contenido del encabezado
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espaciado entre los elementos
        ) {
            // Texto a la izquierda
            Text(
                text = "Ejercicio",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White, // Cambia el color si es necesario
                    fontWeight = FontWeight.Bold
                )
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseFilterView(
    exercises: List<Exercise>,
    onFilterChange: (List<Exercise>) -> Unit
) {
    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedMuscle by remember { mutableStateOf<String?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var searchName by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        // Campo para buscar por nombre
        TextField(
            value = searchName,
            onValueChange = {
                searchName = it
                applyFilters(searchName, selectedType, selectedMuscle, selectedDifficulty, exercises, onFilterChange)
            },
            label = { Text("Search by name") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF007AFF), // Color del borde cuando está en foco
                unfocusedIndicatorColor = Color(0xFFCCCCCC) // Color del borde cuando no está en foco
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown para tipo de ejercicio
        DropdownFilterMenu(
            label = "Type",
            options = listOf("cardio", "strength", "powerlifting", "stretching", "plyometrics", "olympic_weightlifting", "strongman"),
            selectedOption = selectedType,
            onOptionSelected = { option ->
                selectedType = option
                applyFilters(searchName, selectedType, selectedMuscle, selectedDifficulty, exercises, onFilterChange)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown para músculo
        DropdownFilterMenu(
            label = "Muscle",
            options = listOf("abdominals", "biceps", "chest", "glutes", "quadriceps", "triceps", "forearms", "calves", "legs"),
            selectedOption = selectedMuscle,
            onOptionSelected = { option ->
                selectedMuscle = option
                applyFilters(searchName, selectedType, selectedMuscle, selectedDifficulty, exercises, onFilterChange)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown para dificultad
        DropdownFilterMenu(
            label = "Difficulty",
            options = listOf("beginner", "intermediate", "expert"),
            selectedOption = selectedDifficulty,
            onOptionSelected = { option ->
                selectedDifficulty = option
                applyFilters(searchName, selectedType, selectedMuscle, selectedDifficulty, exercises, onFilterChange)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilterMenu(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = selectedOption ?: "Select $label"

    // Utilizando ExposedDropdownMenuBox para el dropdown
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }  // Alternar el estado del menú
    ) {
        TextField(
            value = selectedLabel, // Muestra la opción seleccionada
            onValueChange = { /* Vacío porque es de solo lectura */ },
            readOnly = true, // Solo mostrar la opción seleccionada
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor() // Asegura que el menú se ancle al campo
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF007AFF),
                unfocusedIndicatorColor = Color(0xFFCCCCCC)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun applyFilters(
    searchName: String,
    selectedType: String?,
    selectedMuscle: String?,
    selectedDifficulty: String?,
    exercises: List<Exercise>,
    onFilterChange: (List<Exercise>) -> Unit
) {
    val filteredExercises = exercises.filter {
        (searchName.isBlank() || it.name.contains(searchName, ignoreCase = true)) &&
                (selectedType == null || it.type == selectedType) &&
                (selectedMuscle == null || it.muscle == selectedMuscle) &&
                (selectedDifficulty == null || it.difficulty == selectedDifficulty)
    }
    onFilterChange(filteredExercises)
}

@Composable
fun ExerciseListView(exercises: List<Exercise>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(exercises) { exercise ->
            ExerciseItemView(exercise)
        }
    }
}

@Composable
fun ExerciseItemView(exercise: Exercise) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
        elevation = CardDefaults.cardElevation(4.dp) // Sombra ligera
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {
            // Nombre del ejercicio
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Tipo de ejercicio
            Text(
                text = "Type: ${exercise.type}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray
                )
            )

            // Músculo involucrado
            Text(
                text = "Muscle: ${exercise.muscle}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray
                )
            )

            // Dificultad
            Text(
                text = "Difficulty: ${exercise.difficulty}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Instrucciones
            Text(
                text = exercise.instructions,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Black
                )
            )
        }
    }
}