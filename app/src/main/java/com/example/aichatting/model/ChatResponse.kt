package com.example.aichatting.model

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
