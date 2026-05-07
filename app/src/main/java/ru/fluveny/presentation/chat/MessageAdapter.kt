package ru.fluveny.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fluveny.databinding.ItemMessageAiBinding
import ru.fluveny.databinding.ItemMessageUserBinding
import ru.fluveny.domain.model.AuthorType
import ru.fluveny.domain.model.Message

class MessageAdapter(
    private val onCheckClick: (Message) -> Unit
) : ListAdapter<Message, RecyclerView.ViewHolder>(Diff) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).authorType == AuthorType.USER) VIEW_USER else VIEW_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_USER) {
            UserViewHolder(ItemMessageUserBinding.inflate(inflater, parent, false))
        } else {
            AiViewHolder(ItemMessageAiBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is AiViewHolder -> holder.bind(message)
        }
    }

    inner class UserViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) = with(binding) {
            messageText.text = message.text
            checkButton.setOnClickListener { onCheckClick(message) }
        }
    }

    class AiViewHolder(private val binding: ItemMessageAiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) = with(binding) {
            messageText.text = message.text
        }
    }

    private object Diff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
    }

    private companion object {
        const val VIEW_USER = 1
        const val VIEW_AI = 2
    }
}
