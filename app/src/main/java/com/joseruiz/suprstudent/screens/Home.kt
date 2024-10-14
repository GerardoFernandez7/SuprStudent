package com.joseruiz.suprstudent.screens
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joseruiz.suprstudent.R
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del menú hamburguesa
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Opciones", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                Divider()
                DrawerMenuItem(text = "Perfil", onClick = {
                    Log.e("ERRORRRR", "Errror")
                    navController.navigate("profile")
                })
                DrawerMenuItem(text = "Cerrar sesión", onClick = {
                    navController.navigate("login")
                })
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Encabezado original
            Image(
                painter = painterResource(id = R.drawable.top),
                contentDescription = "Top",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra superior con "Bienvenido"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(bottom = 50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start // Alineado a la izquierda
                ) {
                    //Menu Hamburguesa
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                       HamburgerIcon()
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // Espaciador opcional para separar el icono del texto

                    Text(
                        text = "Bienvenido",
                        color = Color.White,
                        fontSize = 36.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Esto empuja la imagen del logo a la derecha

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterVertically)


                    )
                }

                //Menu del home
                Spacer(modifier = Modifier.height(24.dp))

                MenuItem(imageResId = R.drawable.finanzas, onClick = { navController.navigate("finance") })
                MenuItem(imageResId = R.drawable.academico, onClick = { navController.navigate("academic") })
                MenuItem(imageResId = R.drawable.ejercicio, onClick = { navController.navigate("exercise") })
            }
        }
    }
}
@Composable
fun DrawerMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick)
    )
}


@Composable
fun MenuItem(imageResId: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(25.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp, 150.dp)
                .clip(RoundedCornerShape(15.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}


//Funcion para el botón Hamburguesa
@Composable
fun HamburgerIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val lineHeight = 4.dp.toPx() // Altura de cada línea
        val spacing = 4.dp.toPx() // Espaciado entre líneas
        val totalHeight = (lineHeight * 3) + (spacing * 2) // Altura total

        val startY = (size.height - totalHeight) / 2 // Centrar el icono verticalmente


        drawLine(
            color = Color.White, //
            start = Offset(0f, startY),
            end = Offset(size.width, startY),
            strokeWidth = lineHeight
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, startY + lineHeight + spacing),
            end = Offset(size.width, startY + lineHeight + spacing),
            strokeWidth = lineHeight
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, startY + (lineHeight + spacing) * 2),
            end = Offset(size.width, startY + (lineHeight + spacing) * 2),
            strokeWidth = lineHeight
        )
    }
}

