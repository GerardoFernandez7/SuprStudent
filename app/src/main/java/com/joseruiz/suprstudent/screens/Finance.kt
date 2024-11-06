package com.joseruiz.suprstudent.screens

import android.util.Log
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun FinanceScreen(navController: NavController) {
    var ingreso by remember { mutableStateOf("") }
    var gasto by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var ingresosTotal by remember { mutableStateOf(0.0) }
    var egresosTotal by remember { mutableStateOf(mapOf<String, Double>()) }

    val db = FirebaseFirestore.getInstance()
    val userEmail = FirebaseAuth.getInstance().currentUser?.email
    val coroutineScope = rememberCoroutineScope()

    // Se establece  un listener para los cambios en la base de datos
    LaunchedEffect(userEmail) {
        val userRef = db.collection("finanzas").document(userEmail.toString())
        userRef.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                Log.e("FinanceScreen", "Error al obtener los datos de Firestore: $error")
                return@addSnapshotListener
            }

            // Actualizar los valores de ingresos y egresos cuando cambian
            val currentIngresos = snapshot.getDouble("Ingresos") ?: 0.0
            val currentEgresos = snapshot.get("Egresos") as? Map<String, Double> ?: mapOf(
                "Shopping" to 0.0,
                "Grocery" to 0.0,
                "Bill Payment" to 0.0,
                "Other" to 0.0
            )

            ingresosTotal = currentIngresos
            egresosTotal = currentEgresos
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TextButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "< Atrás", color = Color(0xFF007AFF), fontSize = 20.sp)
        }

        Text(text = "Finanzas", fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        // TextField para ingreso
        OutlinedTextField(
            value = ingreso,
            onValueChange = { ingreso = it },
            label = { Text("Ingreso") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    saveAmountToFirebase(ingreso.toDouble(), "Ingreso", userEmail.toString())
                    ingreso = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8E244D))
        ) {
            Text("Agregar")
        }

        // TextField para gasto
        OutlinedTextField(
            value = gasto,
            onValueChange = { gasto = it },
            label = { Text("Gasto") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        //Checkboxs

        CategoryCheckbox("Shopping", selectedOption) { selectedOption = "Shopping" }
        CategoryCheckbox("Grocery", selectedOption) { selectedOption = "Grocery" }
        CategoryCheckbox("Bill Payment", selectedOption) { selectedOption = "Bill Payment" }
        CategoryCheckbox("Other", selectedOption) { selectedOption = "Other" }

        Button(
            onClick = {
                if (selectedOption.isNotEmpty()) {
                    coroutineScope.launch {
                        saveAmountToFirebase(gasto.toDouble(), selectedOption, userEmail.toString())
                        gasto = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF8E244D))
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Gráfico de pastel
        PieChart(
            data = listOf(
                PieChartData("Shopping", (egresosTotal["Shopping"] ?: 0.0).toFloat() / (ingresosTotal.toFloat()), Color(0xFF42A5F5)),
                PieChartData("Grocery", (egresosTotal["Grocery"] ?: 0.0).toFloat() / (ingresosTotal.toFloat()), Color(0xFFEF5350)),
                PieChartData("Bill Payment", (egresosTotal["Bill Payment"] ?: 0.0).toFloat() / (ingresosTotal.toFloat()), Color(0xFFFFA726)),
                PieChartData("Other", (egresosTotal["Other"] ?: 0.0).toFloat() / (ingresosTotal.toFloat()), Color(0xFF66BB6A))
            ),
            totalAmount = "$${ingresosTotal}"
        )
    }
}
//Funcion para guardar gastos e ingresos
suspend fun saveAmountToFirebase(amount: Double, type: String, user: String) {
    val firestore = FirebaseFirestore.getInstance()
    try {
        val docRef = firestore.collection("finanzas").document(user)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            if (snapshot.exists()) {
                val currentIngresos = snapshot.getDouble("Ingresos") ?: 0.0
                val currentEgresos = snapshot.get("Egresos") as? Map<String, Double> ?: mapOf(
                    "Shopping" to 0.0,
                    "Grocery" to 0.0,
                    "Bill Payment" to 0.0,
                    "Other" to 0.0
                )

                if (type == "Ingreso") {
                    transaction.update(docRef, "Ingresos", currentIngresos + amount)
                } else if (currentEgresos.containsKey(type)) {
                    val updatedEgresos = currentEgresos.toMutableMap()
                    updatedEgresos[type] = updatedEgresos[type]?.plus(amount) ?: amount
                    transaction.update(docRef, "Egresos", updatedEgresos)
                } else {

                }
            } else {
                val newEgresos = mapOf("Shopping" to 0.0, "Grocery" to 0.0, "Bill Payment" to 0.0, "Other" to 0.0)
                val data = hashMapOf("Ingresos" to amount, "Egresos" to newEgresos, "user" to user)
                transaction.set(docRef, data)
            }
        }.await()
    } catch (e: Exception) {
        Log.e("FirestoreError", "Error al guardar los datos: ", e)
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
fun FinanceScreenPreview() {
    FinanceScreen(navController = rememberNavController())
}

e

