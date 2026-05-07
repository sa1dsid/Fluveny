package ru.fluveny.presentation.recommendations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fluveny.databinding.ItemRecommendationBinding
import ru.fluveny.domain.model.Recommendation

class RecommendationAdapter : ListAdapter<Recommendation, RecommendationAdapter.ViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recommendation) = with(binding) {
            titleText.text = "Рекомендации: ${item.language}"
            scoreText.text = "Сложность: ${item.currentComplexityScore}"
            periodText.text = listOfNotNull(item.startDate, item.endDate).joinToString(" - ")
            topicsText.text = item.topicsAnalytics.joinToString("\n") {
                "${it.type}: ошибок ${it.mistakesCount} из ${it.totalMessages}, сложность ${it.complexityScore}"
            }.ifBlank { "Темы появятся после анализа чатов" }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Recommendation>() {
        override fun areItemsTheSame(oldItem: Recommendation, newItem: Recommendation) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Recommendation, newItem: Recommendation) = oldItem == newItem
    }
}
