package com.jschoi.develop.aop_part03_chapter04.api

import com.jschoi.develop.aop_part03_chapter04.model.BestSallerDTO
import com.jschoi.develop.aop_part03_chapter04.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDTO>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSallerDTO>
}