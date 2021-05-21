package com.jschoi.develop.aop_part03_chapter04.model

import com.google.gson.annotations.SerializedName

data class SearchBookDTO(
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)
