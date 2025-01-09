package com.example.myaiapplication.ui.screens.goods.list

import com.example.myaiapplication.domain.model.Goods

data class GoodsListState(
    val goods: List<Goods> = emptyList(),
    val areaIds: List<String> = listOf("全部"),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "",
    val selectedArea: String = "全部"
) 