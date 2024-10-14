package com.joseruiz.suprstudent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joseruiz.suprstudent.screens.AcademicScreen
import com.joseruiz.suprstudent.screens.ExerciseScreen
import com.joseruiz.suprstudent.screens.FinanceScreen
import com.joseruiz.suprstudent.screens.HomeScreen
import com.joseruiz.suprstudent.screens.LoginScreen
import com.joseruiz.suprstudent.screens.ProfileScreen
import com.joseruiz.suprstudent.screens.RegisterScreen
import com.joseruiz.suprstudent.ui.theme.SuprStudentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuprStudentTheme {
                // Manejo de las rutas de la aplicación
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable(route = "login") {
                        LoginScreen(navController = navController)
                    }
                    composable(route = "home") {
                        HomeScreen(navController = navController)
                    }
                    composable(route = "register") {
                        RegisterScreen(navController = navController)
                    }
                    composable(route = "finance") {
                        FinanceScreen(navController)
                    }
                    composable(route = "academic") {
                        AcademicScreen(navController)
                    }
                    composable(route = "exercise") {
                        ExerciseScreen(navController)
                    }
                    composable(route = "profile") {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}
