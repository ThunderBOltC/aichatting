package com.example.aichatting.model

data class ChatMessage(
    val role: String,      // "system" | "user" | "assistant"
    val content: String
)
