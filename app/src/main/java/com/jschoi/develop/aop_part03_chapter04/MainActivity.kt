package com.jschoi.develop.aop_part03_chapter04


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jschoi.develop.aop_part03_chapter04.api.BookService
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://book.interpark.com")
                .addConverterFactory(GsonConverterFactory.create()) // Convert
                .build()

        val bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks("BCB30A5A49176E6BC00D71A170C75E231F3C3407446C1E08648482339A4C509B")
                .enqueue(object : Callback<BestSallerDTO> {
                    override fun onResponse(call: Call<BestSallerDTO>, response: Response<BestSallerDTO>) {
                        // TODO 성공처리
                        if (response.isSuccessful.not()) return

                        response.body()?.let {
                            Log.d(TAG, it.toString())
                            it.books.forEach { book ->
                                Log.d(TAG, book.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<BestSallerDTO>, t: Throwable) {
                        // TODO 실패처리
                        Log.e(TAG, t.toString())
                    }

                })
    }
}