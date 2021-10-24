package com.example.bookreviewmvvm

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookreviewmvvm.adapter.BookAdapter
import com.example.bookreviewmvvm.adapter.HistoryAdapter
import com.example.bookreviewmvvm.api.BookService
import com.example.bookreviewmvvm.api.Constant
import com.example.bookreviewmvvm.databinding.ActivityMainBinding
import com.example.bookreviewmvvm.model.History
import com.example.bookreviewmvvm.model.SearchBookDto
import com.example.bookreviewmvvm.viewmodel.BookViewModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService
    private lateinit var db: AppDatabase

    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        binding.lifecycleOwner = this
        bookViewModel = ViewModelProvider(this, BookViewModelFactory(BookRepository(BookService.getInstance()))).get(BookViewModel::class.java)

        bookViewModel.bestSellerBooks.observe(this, Observer { books ->
            adapter.submitList(books)
        })
        bookViewModel.getBestSellerBooks()

        initBookRecyclerView()
        initHistoryRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        initSearchEditText()

    }

    private fun search(keyword: String) {
        bookService.getBooksByName(Constant.INTERPARK_BOOKS, keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {

                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {

                    hideHistoryView()
                }
            })
    }

    private fun initBookRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
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

    private fun showHistoryView() {
        binding.historyRecyclerView.isVisible = true
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                val keywords = db.historyDao().getAll().reversed()

                withContext(Dispatchers.Main) {
                    binding.historyRecyclerView.isVisible = true
                    historyAdapter.submitList(keywords.orEmpty())
                }
            }
        }
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                db.historyDao().insertHistory(History(null, keyword))
            }
        }
    }

    private fun deleteSearchKeyword(keyword: String) {
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                db.historyDao().delete(keyword)
                showHistoryView()
            }
        }
    }

    override fun onBackPressed() {
        if (binding.historyRecyclerView.isVisible) {
            binding.historyRecyclerView.isVisible = false
            return
        }
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}