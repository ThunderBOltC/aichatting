package com.example.aichatting.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aichatting.R
import com.example.aichatting.ui.model.MessageUi

class ChatAdapter(private val messages: List<MessageUi>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 1
        private const val TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) TYPE_USER else TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            val v = inflater.inflate(R.layout.item_message_user, parent, false)
            UserVH(v)
        } else {
            val v = inflater.inflate(R.layout.item_message_ai, parent, false)
            AIVH(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messages[position]
        when (holder) {
            is UserVH -> holder.bind(item)
            is AIVH -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(m: MessageUi) { txt.text = m.text }
    }

    class AIVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txt: TextView = itemView.findViewById(R.id.textMessage)
        fun bind(m: MessageUi) { txt.text = m.text }
    }
}
