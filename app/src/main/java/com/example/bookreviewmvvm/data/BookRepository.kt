package com.example.bookreviewmvvm.data

import com.example.bookreviewmvvm.api.BookService
import com.example.bookreviewmvvm.api.Constant

class BookRepository constructor(private val retrofitService: BookService) {

    fun getBestSellerBooks() = retrofitService.getBestSellerBooks(Constant.INTERPARK_BOOKS)

}