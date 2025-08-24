package com.example.aichatting.api

import com.example.aichatting.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.openai.com/"

    // 🔹 Interceptor to add Authorization header dynamically
    private val authInterceptor = Interceptor { chain ->
        val apiKey= BuildConfig.OPENAI_API_KEY
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer$apiKey")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    // 🔹 Logging interceptor to see request/response in Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    // 🔹 Retrofit instance
    val instance: OpenAIApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAIApi::class.java)
    }
}
