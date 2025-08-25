package com.example.aichatting

import android.os.Bundle
import android.view.inputmethod.EditorInfo
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
import com.example.aichatting.ui.model.MessageUi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//code is working
class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendBtn: Button

    private val items = mutableListOf<MessageUi>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recyclerView)
        input = findViewById(R.id.inputMessage)
        sendBtn = findViewById(R.id.btnSend)

        adapter = ChatAdapter(items)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        sendBtn.setOnClickListener { submit() }
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) { submit(); true } else false
        }
    }

    private fun submit() {
        val text = input.text?.toString()?.trim().orEmpty()
        if (text.isEmpty()) return
        input.setText("")

        // Show user message
        addMessage(MessageUi(text, isUser = true))

        // Optional: show typing indicator
        addMessage(MessageUi("…", isUser = false))
        val typingIndex = items.lastIndex

        // Build request to OpenRouter (GPT model)
        val request = ChatRequest(
            model = "openai/gpt-4o-mini",
            messages = listOf(
                ChatMessage("system", "You are a helpful assistant."),
                ChatMessage("user", text)
            ),
            max_tokens = 500,
            temperature = 0.7
        )

        RetrofitClient.instance.getChatCompletion(request)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    // remove typing indicator
                    removeAt(typingIndex)

                    if (response.isSuccessful) {
                        val reply = response.body()?.choices?.firstOrNull()?.message?.content
                            ?: "No response"
                        addMessage(MessageUi(reply, isUser = false))
                    } else {
                        addMessage(MessageUi("Error: ${response.errorBody()?.string()}", isUser = false))
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    // remove typing indicator
                    removeAt(typingIndex)
                    addMessage(MessageUi("Failed: ${t.message}", isUser = false))
                }
            })
    }

    private fun addMessage(m: MessageUi) {
        items.add(m)
        adapter.notifyItemInserted(items.lastIndex)
        recycler.scrollToPosition(items.lastIndex)
    }

    private fun removeAt(index: Int) {
        if (index in items.indices) {
            items.removeAt(index)
            adapter.notifyItemRemoved(index)
            recycler.scrollToPosition(items.lastIndex)
        }
    }
}
