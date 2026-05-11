package com.yash.iqtest.api

import com.yash.iqtest.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApi {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount")     amount: Int         = 10,
        @Query("type")       type: String        = "multiple",
        @Query("category")   category: Int?      = null,
        @Query("difficulty") difficulty: String? = null
    ): QuizResponse
}