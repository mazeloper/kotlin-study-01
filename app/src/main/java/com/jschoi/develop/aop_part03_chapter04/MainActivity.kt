package com.jschoi.develop.aop_part03_chapter04


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.jschoi.develop.aop_part03_chapter04.adapter.BookAdapter
import com.jschoi.develop.aop_part03_chapter04.adapter.HistoryAdapter
import com.jschoi.develop.aop_part03_chapter04.api.BookService
import com.jschoi.develop.aop_part03_chapter04.databinding.ActivityMainBinding
import com.jschoi.develop.aop_part03_chapter04.model.BestSallerDTO
import com.jschoi.develop.aop_part03_chapter04.model.History
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
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()
        initRoomDB()

        initRetrofitService()
    }

    private fun initBookRecyclerView() {
        bookAdapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            // 직렬화하여 클래스 그대로 넘김. parcelize 플러그인 추가
            intent.putExtra("EXTRA_BOOK", it)
            startActivity(intent)
        })

        binding.bookRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListner = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = historyAdapter
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        binding.edSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                searchKeyword(binding.edSearch.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.edSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            false
        }
    }

    private fun initRoomDB() {
        db = getAppDatabase(this)

    }

    private fun initRetrofitService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create()) // Convert
            .build()

        bookService = retrofit.create(BookService::class.java)

        searchBestSeller()
    }

    /**
     * 최근 검색어 레이아웃 show
     */
    private fun showHistoryView() {
        Thread {
            // reversed: 최신 순서
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }


    /**
     * 최근 검색어 레이아웃 hide
     */
    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            // View 갱신
            showHistoryView()
        }.start()
    }

    /**
     * BestSeller Search Api
     */
    private fun searchBestSeller() {
        bookService.getBestSellerBooks(getString(R.string.interpark_api_key))
            .enqueue(object : Callback<BestSallerDTO> {
                override fun onResponse(
                    call: Call<BestSallerDTO>,
                    response: Response<BestSallerDTO>
                ) {
                    // 성공처리
                    if (response.isSuccessful.not()) return

                    bookAdapter.submitList(response.body()?.books.orEmpty())    // List Change :: 다시 그려짐
                }

                override fun onFailure(call: Call<BestSallerDTO>, t: Throwable) {
                    // 실패처리
                    Log.e(TAG, t.toString())
                }
            })
    }

    /**
     * Search Api
     */
    private fun searchKeyword(keyword: String) {
        bookService.getBooksByName(getString(R.string.interpark_api_key), keyword)
            .enqueue(object : Callback<SearchBookDTO> {
                override fun onResponse(
                    call: Call<SearchBookDTO>,
                    response: Response<SearchBookDTO>
                ) {
                    if (response.isSuccessful.not()) return

                    hideHistoryView()
                    // DB History Insert
                    saveSearchKeyword(keyword)

                    bookAdapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDTO>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }
}