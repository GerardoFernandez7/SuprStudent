package com.joseruiz.suprstudent.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FinanceScreen() {
    var ingreso by remember { mutableStateOf("") }
    var gasto by remember { mutableStateOf("") }

    // Variable para manejar la opción seleccionada
    var selectedOption by remember { mutableStateOf("") }

    // Agregar un estado para manejar el scroll
    val scrollState = rememberScrollState()

    // Envuelve el contenido de la pantalla en una Column con scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top= 40.dp)
            .verticalScroll(scrollState) // Añadir el scroll vertical aquí
    ) {
        // Título
        Text(
            text = "Finanzas",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // TextField para ingreso
        TextField(
            value = ingreso,
            onValueChange = { ingreso = it },
            label = { Text("Ingreso") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Botón Agregar ingreso
        Button(
            onClick = { /* Acción para agregar ingreso */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8E244D))
        ) {
            Text("Agregar")
        }

        // TextField para gasto
        TextField(
            value = gasto,
            onValueChange = { gasto = it },
            label = { Text("Gasto") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Checkboxes para seleccionar categoría
        Text(
            text = "Selecciona una categoría",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        CategoryCheckbox(
            label = "Shopping",
            selectedOption = selectedOption,
            onSelect = { selectedOption = "Shopping" }
        )
        CategoryCheckbox(
            label = "Grocery",
            selectedOption = selectedOption,
            onSelect = { selectedOption = "Grocery" }
        )
        CategoryCheckbox(
            label = "Bill payment",
            selectedOption = selectedOption,
            onSelect = { selectedOption = "Bill payment" }
        )
        CategoryCheckbox(
            label = "Other",
            selectedOption = selectedOption,
            onSelect = { selectedOption = "Other" }
        )

        // Botón Agregar gasto
        Button(
            onClick = {
                // Validar que se ha seleccionado una categoría antes de agregar el gasto
                if (selectedOption.isNotEmpty()) {
                    // Acción para agregar gasto con la categoría seleccionada
                } else {
                    // Mostrar un mensaje de error o advertencia si no se selecciona categoría
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8E244D))
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Gráfico de pastel con datos quemados
        PieChart(
            data = listOf(
                PieChartData("Shopping", 0.40f, Color(0xFF42A5F5)),
                PieChartData("Grocery", 0.20f, Color(0xFFEF5350)),
                PieChartData("Bill payment", 0.20f, Color(0xFFFFA726)),
                PieChartData("Other", 0.20f, Color(0xFF66BB6A))
            ),
            totalAmount = "$5,639"
        )
    }
}


// Función personalizada para Checkbox de categoría
@Composable
fun CategoryCheckbox(
    label: String,
    selectedOption: String,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = selectedOption == label,
            onCheckedChange = { if (it) onSelect() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}


data class PieChartData(
    val label: String,
    val percentage: Float,
    val color: Color
)

@Composable
fun PieChart(
    data: List<PieChartData>,
    totalAmount: String
) {
    // Columna para mostrar el gráfico y la información debajo
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dibuja el gráfico de pastel
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPieChart(data)
            }

            // Monto total en el centro del gráfico
            Text(
                text = totalAmount,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las categorías con su color y porcentaje
        data.forEach { item ->
            CategoryItem(item.label, item.percentage, item.color)
        }
    }
}

// Función para dibujar la gráfica de pastel
fun DrawScope.drawPieChart(data: List<PieChartData>) {
    var startAngle = 0f
    val pieSize = Size(size.width, size.height)

    data.forEach { pieData ->
        val sweepAngle = pieData.percentage * 360
        drawArc(
            color = pieData.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            size = pieSize
        )
        startAngle += sweepAngle
    }
}

@Composable
fun CategoryItem(name: String, percentage: Float, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, modifier = Modifier.weight(1f))
        Text(text = "${(percentage * 100).toInt()}%")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFinanzasView() {
    FinanceScreen()
}
