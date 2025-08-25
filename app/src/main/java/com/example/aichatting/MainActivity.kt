package com.example.aichatting

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aichatting.api.RetrofitClient
import com.example.aichatting.model.ChatMessage
import com.example.aichatting.model.ChatRequest
import com.example.aichatting.model.ChatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var sendButton: Button
    private lateinit var responseView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("API_TEST", BuildConfig.OPENROUTER_API_KEY)

        inputField = findViewById(R.id.inputField)
        sendButton = findViewById(R.id.sendButton)
        responseView = findViewById(R.id.responseView)

        sendButton.setOnClickListener {
            val userInput = inputField.text.toString()
            if (userInput.isNotBlank()) {
                sendMessageToAPI(userInput)
            }
        }
    }

    private fun sendMessageToAPI(userInput: String) {
        val request = ChatRequest(
            model = "openai/gpt-3.5-turbo",
            messages = listOf(
                ChatMessage("system", "You are a helpful assistant."),
                ChatMessage("user", userInput)
            )
        )

        RetrofitClient.instance.getChatCompletion(request)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(
                    call: Call<ChatResponse>,
                    response: Response<ChatResponse>
                ) {
                    if (response.isSuccessful) {
                        val reply = response.body()?.choices?.firstOrNull()?.message?.content
                        runOnUiThread {
                            responseView.text = reply ?: "No response"
                        }
                    } else {
                        responseView.text = "Error: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    runOnUiThread {
                        responseView.text = "Failed: ${t.message}"
                    }
                }
            })
    }
}
