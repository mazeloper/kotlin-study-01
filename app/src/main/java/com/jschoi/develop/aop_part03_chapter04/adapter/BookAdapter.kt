package com.jschoi.develop.aop_part03_chapter04.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jschoi.develop.aop_part03_chapter04.databinding.ItemBookBinding
import com.jschoi.develop.aop_part03_chapter04.model.Book

/**
 * 북 리스트 어뎁터
 *
 * ViewBinding 사용 예
 */
class BookAdapter(private val itemClickedListener: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    companion object {
        // notifyDataSetChanged 는 지연이 길어지면 UX에 영향을 미치기 때문에, 가능한 적은 리소스와 함께 빠른 작업이 이루어져야 한다.
        // 목록 변경 시 호출하여 아이템 업데이트 하지만 비용이 많이든다.
        // 아를 이해 DiffUtil 클래스가 개발되었다.
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class BookItemViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book) {
            binding.titleTextView.text = item.title
            binding.descTextView.text = item.description

            binding.root.setOnClickListener {
                itemClickedListener(item)
            }

            Glide.with(binding.coverImageView.context)
                .load(item.coverLargeUrl)
                .into(binding.coverImageView)
        }
    }
}