package com.example.aichatting.model

data class ChatRequest(
    val model: String,                 // e.g. "openai/gpt-4o-mini" or "openai/gpt-3.5-turbo"
    val messages: List<ChatMessage>,
    val max_tokens: Int? = 500,        // optional
    val temperature: Double? = 0.7     // optional
)
