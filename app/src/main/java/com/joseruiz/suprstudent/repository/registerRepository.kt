package com.joseruiz.suprstudent.repository

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.joseruiz.suprstudent.data.User
import kotlinx.coroutines.tasks.await

fun register(email: String, password: String, confirmPassword: String, navController: NavController, context: Context) {
    if (email.trim().isNotEmpty() && password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty() && password == confirmPassword) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    saveUserInFirestore(email, password)
                    navController.navigate("login")
                }
            }
            .addOnFailureListener { exception ->
                // Captura el error de Firebase y muestra un mensaje de alerta con el error específico
                Log.e("ERROR_FIREBASE", exception.message.toString())
                showAlert(context, exception.message.toString())
            }
    } else {
        showAlert(context, "Por favor, completa todos los campos y asegúrate de que las contraseñas coincidan.")
    }
}

private fun showAlert(context: Context, message: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Error")
    builder.setMessage(message)
    builder.setPositiveButton("Aceptar", null)
    val dialog: AlertDialog = builder.create()
    dialog.show()  // Mostrar el diálogo
}

private fun saveUserInFirestore(email: String, password: String){
    val db = FirebaseFirestore.getInstance()
    val user = User(email, password)
    // Guardar el usuario en la colección "users" usando el username como ID del documento
    db.collection("users").document(user.username)
        .set(user)
        .addOnSuccessListener {
            println("Usuario guardado en Firestore con éxito")
        }
        .addOnFailureListener { e ->
            println("Error al guardar el usuario en Firestore: ${e.message}")
        }
}


suspend fun getUserData(username: String): User? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val documentSnapshot = db.collection("users").document(username).get().await()
        documentSnapshot.toObject(User::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}