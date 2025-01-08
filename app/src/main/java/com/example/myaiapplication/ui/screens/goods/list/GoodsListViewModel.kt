package com.example.myaiapplication.ui.screens.goods.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaiapplication.domain.model.Goods
import com.example.myaiapplication.domain.repository.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsListViewModel @Inject constructor(
    private val repository: GoodsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoodsListState())
    val uiState: StateFlow<GoodsListState> = _uiState

    init {
        loadGoods()
    }

    fun onEvent(event: GoodsListEvent) {
        when (event) {
            is GoodsListEvent.Refresh -> loadGoods()
            is GoodsListEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                searchGoods(event.query)
            }
            is GoodsListEvent.CategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
                if (event.category.isNotEmpty()) {
                    loadGoodsByCategory(event.category)
                } else {
                    loadGoods()
                }
            }
        }
    }

    private fun loadGoods() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getAllGoods()
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { goods ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            goods = goods,
                            error = null
                        )
                    }
                }
        }
    }

    private fun searchGoods(query: String) {
        if (query.isEmpty()) {
            loadGoods()
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.searchGoods(query)
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { goods ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            goods = goods,
                            error = null
                        )
                    }
                }
        }
    }

    private fun loadGoodsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getGoodsByCategory(category)
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect { goods ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            goods = goods,
                            error = null
                        )
                    }
                }
        }
    }
} 