package com.joseruiz.suprstudent.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joseruiz.suprstudent.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = "Académico",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

//Función que captura los datos de la base de datos y lo muestra en la vista
@Composable
fun AcademicScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    val tasks = remember { mutableStateListOf<Map<String, Any>>() }

    // Escucha activa a Firestore
    LaunchedEffect(Unit) {
        db.collection("academico")
            .whereEqualTo("usuario", userEmail)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    tasks.clear()
                    for (document in snapshot) {
                        val taskData = document.data.toMutableMap()
                        taskData["id"] = document.id // Añade el ID del documento al mapa de datos
                        tasks.add(taskData)
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 40.dp)
    ) {
        TextButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "< Atrás",
                color = Color(0xFF007AFF),
                fontSize = 20.sp
            )
        }

        TopBar()
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de agregar una TaskCard
        AddButton()
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las TaskCards desde Firestore
        LazyColumn {
            items(tasks) { task ->
                TaskCard(
                    taskId = task["id"] as String, // Pasamos el ID del documento para poder actualizarlo
                    subject = task["tarea"] as String,
                    time = task["hora"] as String,
                    description = task["descripcion"] as String,
                    done = task["estado"] as? Boolean ?: false // Se obtiene el estado o false si no existe
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

//Función para abrir el modal, para guardar una actividad
@Composable
fun AddButton() {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val subject = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    // Obtener el usuario actual
    val user = FirebaseAuth.getInstance().currentUser?.email

    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { showDialog.value = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(context, R.color.customMaroon))),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(0.5f).padding(vertical = 15.dp)
        ) {
            Text(text = "Agregar", color = Color.White)
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Nueva Tarea") },
            text = {
                Column(
                    modifier = Modifier.padding(10.dp).background(Color.White, RoundedCornerShape(8.dp)),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = subject.value,
                        onValueChange = { subject.value = it },
                        label = { Text("Tarea") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = time.value,
                        onValueChange = { time.value = it },
                        label = { Text("Hora") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (subject.value.isNotEmpty() && time.value.isNotEmpty()) {
                            isUploading = true
                            uploadError = null

                            coroutineScope.launch {
                                val result = saveTaskToFirebase(subject.value, time.value, description.value, user.toString())
                                isUploading = false
                                if (result == null) {
                                    showDialog.value = false
                                    Toast.makeText(context, "Tarea guardada", Toast.LENGTH_SHORT).show()
                                } else {
                                    uploadError = result
                                }
                            }
                        } else {
                            uploadError = "La tarea y la hora son obligatorias."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(context, R.color.customMaroon))),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(ContextCompat.getColor(context, R.color.customMaroon)))
                ) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(15.dp),
            containerColor = Color.White
        )
    }

    if (isUploading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    uploadError?.let { error ->
        Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
    }
}

// Función para guardar la tarea en Firestore
suspend fun saveTaskToFirebase(subject: String, time: String, description: String, user: String): String? {
    val firestore = FirebaseFirestore.getInstance()
    return try {
        val taskData = hashMapOf(
            "tarea" to subject,
            "hora" to time,
            "descripcion" to description,
            "usuario" to user,
            "estado" to false // La tarea se crea como pendiente
        )

        firestore.collection("academico").add(taskData).await()
        null
    } catch (e: Exception) {
        Log.e("FirestoreError", "Error al guardar la tarea: ", e)
        e.localizedMessage ?: "Error al guardar la tarea"
    }
}



//Función de la vista de las cards
@Composable
fun TaskCard(
    taskId: String,
    subject: String,
    time: String,
    description: String,
    done: Boolean
) {
    val db = FirebaseFirestore.getInstance()
    val buttonColor = if (done) Color(0xFF28a745) else Color(0xFFFFD700)

    Card(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = subject, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = description, fontSize = 14.sp, color = Color.Gray)
                }
                Text(text = time, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Cambia el estado en Firestore al presionar el botón
                        db.collection("academico").document(taskId)
                            .update("estado", !done)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = if (done) "Hecho" else "Pendiente", color = Color.White)
                }
                // Botones de edición y eliminación
                Row {
                    IconButton(onClick = { /* Editar una taskcard */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    IconButton(onClick = { /* Eliminar una taskcard */ }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }
    }
}



