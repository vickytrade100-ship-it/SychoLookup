package com.sycho.lookup.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val query: String,
    val type: String,
    val resultsCount: Int,
    val topName: String?,
    val timestamp: Long = System.currentTimeMillis()
)
