package com.joseruiz.suprstudent.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.joseruiz.suprstudent.R
import com.joseruiz.suprstudent.data.User
import com.joseruiz.suprstudent.repository.getUserData
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val username = FirebaseAuth.getInstance().currentUser?.email

    val coroutineScope = rememberCoroutineScope()

    // Estados para los campos
    var userData by remember { mutableStateOf(User(username = username.toString())) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar los datos del usuario
    LaunchedEffect(key1 = username) {
        coroutineScope.launch {
            val user = getUserData(username.toString())
            if (user != null) {
                userData = user
            }
            isLoading = false
        }
    }

    if (isLoading) {
        // Usamos un Box para centrar el indicador de carga
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Perfil",
                    color = Color.Black,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "User",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Text(
                text = "Datos Usuario",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.Start)
            )

            OutlinedTextField(
                value = userData.username,
                onValueChange = { userData = userData.copy(username = it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = false // Para que no se pueda editar
            )

            OutlinedTextField(
                value = userData.name,
                onValueChange = { userData = userData.copy(name = it) },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = userData.age,
                onValueChange = { userData = userData.copy(age = it) },
                label = { Text("Edad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = userData.gender,
                onValueChange = { userData = userData.copy(gender = it) },
                label = { Text("Género") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = { /* Aquí puedes añadir la lógica para guardar los cambios en Firestore */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78203A)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Guardar",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}