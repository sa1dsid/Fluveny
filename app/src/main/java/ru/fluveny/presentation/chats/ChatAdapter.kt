package ru.fluveny.presentation.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fluveny.databinding.ItemChatBinding
import ru.fluveny.domain.model.Chat

class ChatAdapter(
    private val onClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) = with(binding) {
            titleText.text = chat.name.ifBlank { "Без названия" }
            metaText.text = listOf(chat.language, chat.type).filter { it.isNotBlank() }.joinToString(" • ")
            descriptionText.text = chat.description.orEmpty().ifBlank { "Описание не указано" }
            dateText.text = chat.createdAt.orEmpty()
            root.setOnClickListener { onClick(chat) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
    }
}
