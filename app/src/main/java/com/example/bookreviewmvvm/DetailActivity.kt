package com.example.bookreviewmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.bookreviewmvvm.data.AppDatabase
import com.example.bookreviewmvvm.databinding.ActivityDetailBinding
import com.example.bookreviewmvvm.data.model.Book
import com.example.bookreviewmvvm.data.model.Review
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DetailActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                val review = db.reviewDao().getOneReview(model?.id ?: 0)
                withContext(Dispatchers.Main) {
                    binding.reviewEditText.setText(review?.review.orEmpty())
                }
            }
        }

        binding.saveButton.setOnClickListener {
            launch(coroutineContext) {
                withContext(Dispatchers.IO) {
                    db.reviewDao().saveReview(
                        Review(
                            model?.id ?: 0,
                            binding.reviewEditText.text.toString()
                        )
                    )
                }
            }
        }
    }
}