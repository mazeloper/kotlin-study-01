package com.jschoi.develop.aop_part03_chapter04


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jschoi.develop.aop_part03_chapter04.adapter.BookAdapter
import com.jschoi.develop.aop_part03_chapter04.api.BookService
import com.jschoi.develop.aop_part03_chapter04.databinding.ActivityMainBinding
import com.jschoi.develop.aop_part03_chapter04.model.BestSallerDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 인터파크 도서 검색 앱
 *
 * Retrofit2
 * recyclerView
 * View Binding
 * Android Room
 * Glide
 */
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        createRetrofitService()

    }

    private fun initBookRecyclerView() {
        bookAdapter = BookAdapter()

        binding.bookRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }
    }

    private fun createRetrofitService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create()) // Convert
            .build()

        val bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks("BCB30A5A49176E6BC00D71A170C75E231F3C3407446C1E08648482339A4C509B")
            .enqueue(object : Callback<BestSallerDTO> {
                override fun onResponse(
                    call: Call<BestSallerDTO>,
                    response: Response<BestSallerDTO>
                ) {
                    // 성공처리
                    if (response.isSuccessful.not()) return

                    response.body()?.let {
                        Log.d(TAG, it.toString())
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        // List Change :: 다시 그려짐
                        bookAdapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSallerDTO>, t: Throwable) {
                    // 실패처리
                    Log.e(TAG, t.toString())
                }
            })
    }
}