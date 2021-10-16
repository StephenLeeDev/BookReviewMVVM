package com.example.bookreviewmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookreviewmvvm.adapter.BookAdapter
import com.example.bookreviewmvvm.api.BookService
import com.example.bookreviewmvvm.api.Uri
import com.example.bookreviewmvvm.databinding.ActivityMainBinding
import com.example.bookreviewmvvm.model.BestSellerDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl(Uri.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.INTERPARK_BOOKS))
            .enqueue(object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let { body ->
                        adapter.submitList(body.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {

                }

            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()

    companion object {
        private const val TAG = "MainActivity"
    }
}