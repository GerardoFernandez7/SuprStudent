package com.joseruiz.suprstudent.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.joseruiz.suprstudent.R

@Composable
fun AcademicScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TopBar()
        Spacer(modifier = Modifier.height(16.dp))
        //Boton de agregar un taskCard
        AddButton()
        Spacer(modifier = Modifier.height(16.dp))
        //TaskCard quemadas
        TaskCard(subject = "Matemática", time = "10:00 am", description = "Estudiar módulo 1 y 2", doneColor = Color(0xFF28a745))
        Spacer(modifier = Modifier.height(18.dp))
        TaskCard(subject = "POO", time = "15:00 pm", description = "Laboratorio no. 1", doneColor = Color(0xFF28a745))

    }
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { /* Regresar */ }) {
            Text(text = "Atrás", color = Color(0xFF007AFF))
        }
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

@Composable
fun AddButton() {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val subject = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

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

    //Modal
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Nueva Tarea") },
            text = {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.White, RoundedCornerShape(8.dp)),
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
                        // Aquí puedes manejar la lógica para agregar la TaskCard
                        showDialog.value = false
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
            shape = RoundedCornerShape(15.dp), // Forma del diálogo
            containerColor = Color.White // Fondo del diálogo
        )
    }
}

@Composable
fun TaskCard(subject: String, time: String, description: String, doneColor: Color) {
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
                    onClick = { /*Seleccionar si ya esta hecho la tarea o no */ },
                    colors = ButtonDefaults.buttonColors(containerColor = doneColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Hecho", color = Color.White)
                }
                Row {
                    IconButton(onClick = { /* Editar una taskcard */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit, // Utiliza un ícono nativo
                            contentDescription = "Edit",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    IconButton(onClick = { /* Eliminar una taskcard */ }) {
                        Icon(
                            imageVector = Icons.Default.Delete, // Utiliza un ícono nativo
                            contentDescription = "Delete",
                            modifier = Modifier.size(34.dp)
                        )
                    }

                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun AcademicScreenPreview() {
    AcademicScreen()
}
