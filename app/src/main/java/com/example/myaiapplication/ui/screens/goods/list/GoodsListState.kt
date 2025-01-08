package com.example.myaiapplication.ui.screens.goods.list

import com.example.myaiapplication.domain.model.Goods

data class GoodsListState(
    val goods: List<Goods> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = ""
) 