package com.sycho.lookup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sycho.lookup.data.local.SearchHistoryEntity
import com.sycho.lookup.data.model.LookupResponse
import com.sycho.lookup.data.remote.NetworkResult
import com.sycho.lookup.repository.LookupRepository
import com.sycho.lookup.utils.isValidQuery
import com.sycho.lookup.utils.sanitizeQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookupViewModel @Inject constructor(
    private val repository: LookupRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _lookupState = MutableStateFlow<NetworkResult<LookupResponse>>(NetworkResult.Idle)
    val lookupState: StateFlow<NetworkResult<LookupResponse>> = _lookupState.asStateFlow()

    private val _inputError = MutableStateFlow<String?>(null)
    val inputError: StateFlow<String?> = _inputError.asStateFlow()

    val searchHistory: StateFlow<List<SearchHistoryEntity>> =
        repository.getSearchHistory()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(value: String) {
        _query.value = value
        if (_inputError.value != null) _inputError.value = null
    }

    fun search() {
        val raw = _query.value.trim()
        if (!raw.isValidQuery()) {
            _inputError.value = "Enter a valid mobile number (starting with 92) or 13-digit CNIC"
            return
        }
        val sanitized = raw.sanitizeQuery()
        viewModelScope.launch {
            repository.lookup(sanitized).collect { _lookupState.value = it }
        }
    }

    fun searchFromHistory(query: String) {
        _query.value = query
        search()
    }

    fun deleteHistoryEntry(entity: SearchHistoryEntity) {
        viewModelScope.launch { repository.deleteHistoryEntry(entity) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearAllHistory() }
    }

    fun resetState() {
        _lookupState.value = NetworkResult.Idle
        _inputError.value = null
    }
}
