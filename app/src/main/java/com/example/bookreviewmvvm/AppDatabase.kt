package com.example.bookreviewmvvm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookreviewmvvm.dao.HistoryDao
import com.example.bookreviewmvvm.dao.ReviewDao
import com.example.bookreviewmvvm.model.History
import com.example.bookreviewmvvm.model.Review

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}