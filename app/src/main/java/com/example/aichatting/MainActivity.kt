package com.example.aichatting

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aichatting.api.RetrofitClient
import com.example.aichatting.model.ChatMessage
import com.example.aichatting.model.ChatRequest
import com.example.aichatting.model.ChatResponse
import com.example.aichatting.ui.ChatAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var editMessage: EditText
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        editMessage = findViewById(R.id.editMessage)
        btnSend = findViewById(R.id.btnSend)

        adapter = ChatAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        Log.d("API_KEY_TEST", "Loaded API Key: ${BuildConfig.OPENAI_API_KEY}")

        btnSend.setOnClickListener {
            val userMessage = editMessage.text.toString()
            if (userMessage.isNotBlank()) {
                // Show user message
                adapter.addMessage(ChatMessage("user", userMessage))
                recyclerView.scrollToPosition(adapter.itemCount - 1)
                editMessage.text.clear()

                // Send to OpenAI API
                sendMessageToApi(userMessage)
            }
        }
    }

    private fun sendMessageToApi(userInput: String) {
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
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
                        if (!reply.isNullOrBlank()) {
                            adapter.addMessage(ChatMessage("assistant", reply))
                            recyclerView.scrollToPosition(adapter.itemCount - 1)
                            Log.d("API_RESPONSE", reply)
                        } else {
                            adapter.addMessage(ChatMessage("assistant", "⚠️ No reply from API"))
                            Log.e("API_ERROR", "Empty response body: ${response.body()}")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        adapter.addMessage(ChatMessage("assistant", "❌ Error: $errorBody"))
                        Log.e("API_ERROR", "Response failed: $errorBody")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    adapter.addMessage(ChatMessage("assistant", "❌ Failure: ${t.message}"))
                    Log.e("API_ERROR", "API call failed", t)
                }
            })
    }
}
