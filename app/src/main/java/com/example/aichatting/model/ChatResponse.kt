package com.example.aichatting.model

data class ChatResponse(
    val id: String? = null,
    val choices: List<Choice> = emptyList()
)

data class Choice(
    val index: Int = 0,
    val message: ChatMessage,
    val finish_reason: String? = null
)
