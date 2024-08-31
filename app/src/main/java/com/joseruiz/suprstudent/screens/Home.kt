package com.joseruiz.suprstudent.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseruiz.suprstudent.R

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(bottom = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bienvenido",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            // Menu
            Spacer(modifier = Modifier.height(24.dp))

            MenuItem(imageResId = R.drawable.finanzas, onClick = { /* Navegar a Finanzas */ })
            MenuItem(imageResId = R.drawable.academico, onClick = { /* Navegar a Académico */ })
            MenuItem(imageResId = R.drawable.ejercicio, onClick = { /* Navegar a Ejercicio */ })

        }
    }
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