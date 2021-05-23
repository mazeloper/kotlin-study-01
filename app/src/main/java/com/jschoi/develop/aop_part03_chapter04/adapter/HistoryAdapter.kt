package com.jschoi.develop.aop_part03_chapter04.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jschoi.develop.aop_part03_chapter04.databinding.ItemHistoryBinding
import com.jschoi.develop.aop_part03_chapter04.model.History

class HistoryAdapter(val historyDeleteClickListner: (String) -> Unit) :
    ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    companion object {
        // notifyDataSetChanged 는 지연이 길어지면 UX에 영향을 미치기 때문에, 가능한 적은 리소스와 함께 빠른 작업이 이루어져야 한다.
        // 목록 변경 시 호출하여 아이템 업데이트 하지만 비용이 많이든다.
        // 아를 이해 DiffUtil 클래스가 개발되었다.
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryItemViewHolder {
        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
            binding.historyKeywordTextView.text = historyModel.keyword
            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickListner(historyModel.keyword.orEmpty())
            }
        }
    }
}