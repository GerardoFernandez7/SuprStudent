package com.joseruiz.suprstudent.api

import com.joseruiz.suprstudent.data.Exercise
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Correcto interceptor para OkHttp
val apiKeyInterceptor = Interceptor { chain ->
    val original = chain.request()
    val requestBuilder: Request.Builder = original.newBuilder()
        .header("X-Api-Key", "SgAE+RN92D9KkVJGCUtCew==71gLiF24K1GgdfaE")
    val request: Request = requestBuilder.build()
    chain.proceed(request)
}

// Configuración del cliente OkHttp con el interceptor
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(apiKeyInterceptor)
    .build()

// Configuración de Retrofit
private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.api-ninjas.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Servicio para hacer la llamada a la API
val exerciseService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("exercises")
    suspend fun getMuscles(@Query("muscle") muscle: String): List<Exercise>

    @GET("exercises")
    suspend fun getTypes(@Query("type") type: String): List<Exercise>

    @GET("exercises")
    suspend fun getDifficulty(@Query("difficulty") difficulty: String): List<Exercise>
}