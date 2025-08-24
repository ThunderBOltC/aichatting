package com.example.aichatting.model


data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)