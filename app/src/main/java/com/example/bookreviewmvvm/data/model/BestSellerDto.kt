package com.example.bookreviewmvvm.data.model


import com.google.gson.annotations.SerializedName

data class BestSellerDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("item")
    val books: List<Book>
)