package com.example.aichatting.model



data class ChatMessage(
    val role: String,   // "user" or "assistant"
    val content: String
)
