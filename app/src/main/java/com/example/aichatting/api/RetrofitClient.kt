package com.example.aichatting.api

import com.example.aichatting.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://openrouter.ai/api/"

    private val authInterceptor = Interceptor { chain ->
        val apiKey = BuildConfig.OPENROUTER_API_KEY
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("HTTP-Referer", "http://localhost")  // put your app/site URL if you have one
            .addHeader("X-Title", "AIChattingApp")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()

    val instance: OpenRouterApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenRouterApi::class.java)
    }
}
