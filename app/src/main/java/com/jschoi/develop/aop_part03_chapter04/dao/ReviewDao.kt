package com.jschoi.develop.aop_part03_chapter04.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jschoi.develop.aop_part03_chapter04.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id == :id")
    fun getOneReview(id: Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 똑같은 아이디 값에 데이터가 있으면 교체
    fun saveReview(review: Review)

}