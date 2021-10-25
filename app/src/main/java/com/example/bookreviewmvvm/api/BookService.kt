package com.example.bookreviewmvvm.api

import com.example.bookreviewmvvm.data.model.BestSellerDto
import com.example.bookreviewmvvm.data.model.SearchBookDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String,
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>

    companion object {

        var retrofitService: BookService? = null

        fun getInstance() : BookService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Uri.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(BookService::class.java)
            }
            return retrofitService!!
        }
    }
}