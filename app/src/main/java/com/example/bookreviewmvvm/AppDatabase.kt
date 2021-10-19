package com.example.bookreviewmvvm

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookreviewmvvm.dao.HistoryDao
import com.example.bookreviewmvvm.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}