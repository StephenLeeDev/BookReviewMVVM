package com.example.bookreviewmvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreviewmvvm.viewmodel.BookViewModel

class BookViewModelFactory constructor(private val repository: BookRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            BookViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}