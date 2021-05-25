package com.jschoi.develop.aop_part03_chapter04

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.jschoi.develop.aop_part03_chapter04.databinding.ActivityDetailBinding
import com.jschoi.develop.aop_part03_chapter04.model.Book
import com.jschoi.develop.aop_part03_chapter04.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("EXTRA_BOOK")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descTextView.text = model?.description.orEmpty()

        Glide.with(this)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)


        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0, binding.reviewEditText.text.toString())
                )
                finish()
            }.start()
        }
    }
}