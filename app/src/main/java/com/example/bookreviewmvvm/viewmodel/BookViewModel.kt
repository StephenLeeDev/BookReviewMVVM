package com.example.bookreviewmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookreviewmvvm.BookRepository
import com.example.bookreviewmvvm.model.BestSellerDto
import com.example.bookreviewmvvm.model.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel constructor(private val repository: BookRepository)  : ViewModel() {

    val bestSellerBooks = MutableLiveData<List<Book>>()
    val errorMessage = MutableLiveData<String>()

    fun getBestSellerBooks() = repository.getBestSellerBooks()
        .enqueue(object : Callback<BestSellerDto> {
            override fun onResponse(
                call: Call<BestSellerDto>,
                response: Response<BestSellerDto>
            ) {

                if (response.isSuccessful) {
                    bestSellerBooks.postValue(response.body()?.books.orEmpty())
                }
            }

            override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
}
