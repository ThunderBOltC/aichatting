package com.example.aichatting.api

import com.example.aichatting.model.ChatRequest
import com.example.aichatting.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIApi {
    @POST("v1/chat/completions")
    fun getChatCompletion(@Body request: ChatRequest): Call<ChatResponse>
}

