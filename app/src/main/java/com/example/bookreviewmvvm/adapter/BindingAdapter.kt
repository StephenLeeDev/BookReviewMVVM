package com.example.bookreviewmvvm.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bookreviewmvvm.DetailActivity
import com.example.bookreviewmvvm.data.model.Book

class BindingAdapter {

    companion object {

        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().fitCenter())
                .into(imageView)
        }

        @BindingAdapter("app:onBookClick")
        @JvmStatic
        fun onBookClick(view: View, bookModel: Book) {
            view.setOnClickListener {
                val context: Context = view.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("bookModel", bookModel)
                context.startActivity(intent)
            }
        }
    }
}