package com.example.myaiapplication.ui.screens.analytics

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
class AnalyticsViewModel @Inject constructor(
    private val repository: GoodsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState

    init {
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
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
                    // 处理数据分析逻辑
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            goodsCount = goods.size,
                            error = null
                        )
                    }
                }
        }
    }
}

// 定义UI状态数据类
data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val goodsCount: Int = 0,
    val error: String? = null
) 