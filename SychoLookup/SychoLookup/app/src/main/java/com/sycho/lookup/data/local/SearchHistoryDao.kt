package com.sycho.lookup.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(entity: SearchHistoryEntity)

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    fun getAllSearches(): Flow<List<SearchHistoryEntity>>

    @Delete
    suspend fun deleteSearch(entity: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()

    @Query("SELECT * FROM search_history WHERE query = :query LIMIT 1")
    suspend fun getByQuery(query: String): SearchHistoryEntity?
}
