package com.example.bookreviewmvvm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookreviewmvvm.data.dao.HistoryDao
import com.example.bookreviewmvvm.data.dao.ReviewDao
import com.example.bookreviewmvvm.data.model.History
import com.example.bookreviewmvvm.data.model.Review

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}