package com.example.myaiapplication.ui.screens.goods.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaiapplication.domain.model.Goods
import com.example.myaiapplication.domain.model.GoodsStatus
import com.example.myaiapplication.domain.model.Location
import com.example.myaiapplication.domain.model.PurchaseInfo
import com.example.myaiapplication.domain.repository.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GoodsEditViewModel @Inject constructor(
    private val repository: GoodsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val goodsId: Long = savedStateHandle["goodsId"] ?: -1L
    
    private val _uiState = MutableStateFlow(GoodsEditState())
    val uiState: StateFlow<GoodsEditState> = _uiState

    init {
        if (goodsId != -1L) {
            loadGoods()
        }
    }

    fun onEvent(event: GoodsEditEvent) {
        when (event) {
            is GoodsEditEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is GoodsEditEvent.DescriptionChanged -> {
                _uiState.update { it.copy(description = event.description) }
            }
            is GoodsEditEvent.BrandChanged -> {
                _uiState.update { it.copy(brand = event.brand) }
            }
            is GoodsEditEvent.CategoryChanged -> {
                _uiState.update { it.copy(category = event.category) }
            }
            is GoodsEditEvent.StatusChanged -> {
                _uiState.update { it.copy(status = event.status) }
            }
            is GoodsEditEvent.LocationChanged -> {
                _uiState.update { it.copy(location = event.location) }
            }
            is GoodsEditEvent.PurchaseInfoChanged -> {
                _uiState.update { it.copy(purchaseInfo = event.purchaseInfo) }
            }
            is GoodsEditEvent.TagAdded -> {
                val currentTags = _uiState.value.tags.toMutableList()
                if (!currentTags.contains(event.tag)) {
                    currentTags.add(event.tag)
                    _uiState.update { it.copy(tags = currentTags) }
                }
            }
            is GoodsEditEvent.TagRemoved -> {
                val currentTags = _uiState.value.tags.toMutableList()
                currentTags.remove(event.tag)
                _uiState.update { it.copy(tags = currentTags) }
            }
            is GoodsEditEvent.AttributeAdded -> {
                val currentAttributes = _uiState.value.attributes.toMutableMap()
                currentAttributes[event.key] = event.value
                _uiState.update { it.copy(attributes = currentAttributes) }
            }
            is GoodsEditEvent.AttributeRemoved -> {
                val currentAttributes = _uiState.value.attributes.toMutableMap()
                currentAttributes.remove(event.key)
                _uiState.update { it.copy(attributes = currentAttributes) }
            }
            is GoodsEditEvent.PhotoAdded -> {
                val currentPhotos = _uiState.value.photoUrls.toMutableList()
                if (!currentPhotos.contains(event.url)) {
                    currentPhotos.add(event.url)
                    _uiState.update { it.copy(photoUrls = currentPhotos) }
                }
            }
            is GoodsEditEvent.PhotoRemoved -> {
                val currentPhotos = _uiState.value.photoUrls.toMutableList()
                currentPhotos.remove(event.url)
                _uiState.update { it.copy(photoUrls = currentPhotos) }
            }
            is GoodsEditEvent.PhotosChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(photoUrls = event.photoUrls)
                }
            }
            GoodsEditEvent.Save -> saveGoods()
        }
    }

    private fun loadGoods() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getGoodsById(goodsId).collect { goods ->
                    goods?.let {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                name = goods.name,
                                description = goods.description ?: "",
                                brand = goods.brand ?: "",
                                category = goods.category,
                                status = goods.status,
                                location = goods.location,
                                purchaseInfo = goods.purchaseInfo,
                                tags = goods.tags,
                                attributes = goods.attributes,
                                photoUrls = goods.photoUrls
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun saveGoods() {
        val state = _uiState.value
        
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "名称不能为空") }
            return
        }
        if (state.category.isBlank()) {
            _uiState.update { it.copy(error = "类别不能为空") }
            return
        }

        val goods = Goods(
            id = goodsId.takeIf { it != -1L } ?: 0,
            name = state.name,
            description = state.description.takeIf { it.isNotBlank() },
            brand = state.brand.takeIf { it.isNotBlank() },
            category = state.category,
            status = state.status,
            location = state.location,
            purchaseInfo = state.purchaseInfo,
            tags = state.tags,
            attributes = state.attributes,
            photoUrls = state.photoUrls,
            createTime = Date(),
            updateTime = Date()
        )

        viewModelScope.launch {
            try {
                if (goodsId == -1L) {
                    repository.insertGoods(goods)
                } else {
                    repository.updateGoods(goods)
                }
                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }
} 