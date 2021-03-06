package com.jschoi.develop.aop_part03_chapter04.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    // 실제로는 itemId로 내려온다. == SerializedName 어노테이션 정의
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String,
    @SerializedName("coverLargeUrl") val coverLargeUrl: String
) : Parcelable