package com.joseruiz.suprstudent.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.joseruiz.suprstudent.R


@Composable
fun ProfileScreen(navController: NavController) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                TextButton(
                    onClick = { navController.navigate("home")  },
                    modifier = Modifier.align(Alignment.Start) // Alinea el botón en la esquina superior izquierda
                ) {
                    Text(
                        text = "< Atrás",
                        color = Color(0xFF007AFF),
                        fontSize = 20.sp // Ajusta el tamaño de la fuente
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
                    .align(Alignment.Start) // Alinear a la izquierda
            )

            // Campos de entrada
            OutlinedTextField (
                value = "",
                onValueChange = { /* TODO */ },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField (
                value = "",
                onValueChange = { /* TODO */ },
                label = { Text("Edad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField (
                value = "",
                onValueChange = { /* TODO */ },
                label = { Text("Género") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(70.dp))

            // Botón de guardar
            Button(
                onClick = {  navController.navigate("home")  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78203A)), // Color del botón
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
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}