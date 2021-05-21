package com.jschoi.develop.aop_part03_chapter04


import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jschoi.develop.aop_part03_chapter04.adapter.BookAdapter
import com.jschoi.develop.aop_part03_chapter04.api.BookService
import com.jschoi.develop.aop_part03_chapter04.databinding.ActivityMainBinding
import com.jschoi.develop.aop_part03_chapter04.model.BestSallerDTO
import com.jschoi.develop.aop_part03_chapter04.model.SearchBookDTO
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

    private lateinit var bookService: BookService
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        createRetrofitService()

        binding.edSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.edSearch.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interpark_api_key), keyword)
            .enqueue(object : Callback<SearchBookDTO> {
                override fun onResponse(
                    call: Call<SearchBookDTO>,
                    response: Response<SearchBookDTO>
                ) {
                    if (response.isSuccessful.not()) return

                    bookAdapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDTO>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
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

        bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks(getString(R.string.interpark_api_key))
            .enqueue(object : Callback<BestSallerDTO> {
                override fun onResponse(
                    call: Call<BestSallerDTO>,
                    response: Response<BestSallerDTO>
                ) {
                    // 성공처리
                    if (response.isSuccessful.not()) return

                    response.body()?.let {
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        bookAdapter.submitList(it.books)    // List Change :: 다시 그려짐
                    }
                }

                override fun onFailure(call: Call<BestSallerDTO>, t: Throwable) {
                    // 실패처리
                    Log.e(TAG, t.toString())
                }
            })
    }
}