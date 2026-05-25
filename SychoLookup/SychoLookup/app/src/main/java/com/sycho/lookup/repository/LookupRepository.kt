package com.sycho.lookup.repository

import com.sycho.lookup.data.local.SearchHistoryDao
import com.sycho.lookup.data.local.SearchHistoryEntity
import com.sycho.lookup.data.model.LookupResponse
import com.sycho.lookup.data.remote.ApiService
import com.sycho.lookup.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LookupRepository @Inject constructor(
    private val apiService: ApiService,
    private val searchHistoryDao: SearchHistoryDao
) {
    fun lookup(query: String): Flow<NetworkResult<LookupResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = apiService.lookup(query)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    saveToHistory(body)
                    emit(NetworkResult.Success(body))
                } else {
                    emit(NetworkResult.Error("No results found for \"$query\""))
                }
            } else {
                emit(NetworkResult.Error("Server error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Unknown network error"))
        }
    }

    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> = searchHistoryDao.getAllSearches()

    suspend fun deleteHistoryEntry(entity: SearchHistoryEntity) = searchHistoryDao.deleteSearch(entity)

    suspend fun clearAllHistory() = searchHistoryDao.clearHistory()

    private suspend fun saveToHistory(response: LookupResponse) {
        val topName = response.results.firstOrNull { !it.name.isNullOrBlank() }?.name
        searchHistoryDao.getByQuery(response.query)?.let { searchHistoryDao.deleteSearch(it) }
        searchHistoryDao.insertSearch(
            SearchHistoryEntity(
                query = response.query,
                type = response.type,
                resultsCount = response.resultsCount,
                topName = topName
            )
        )
    }
}
