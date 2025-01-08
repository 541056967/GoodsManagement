package com.example.myaiapplication.ui.screens.goods.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaiapplication.domain.repository.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsDetailViewModel @Inject constructor(
    private val repository: GoodsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val goodsId: Long = checkNotNull(savedStateHandle["goodsId"])
    
    private val _uiState = MutableStateFlow(GoodsDetailState())
    val uiState: StateFlow<GoodsDetailState> = _uiState

    init {
        loadGoods()
    }

    fun onEvent(event: GoodsDetailEvent) {
        when (event) {
            is GoodsDetailEvent.DeleteGoods -> deleteGoods()
        }
    }

    private fun loadGoods() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getGoodsById(goodsId)
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

    private fun deleteGoods() {
        viewModelScope.launch {
            try {
                repository.deleteGoods(goodsId)
                _uiState.update { it.copy(isDeleted = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }
} 