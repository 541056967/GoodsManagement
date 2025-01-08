package com.example.myaiapplication.ui.screens.goods.detail

import com.example.myaiapplication.domain.model.Goods

data class GoodsDetailState(
    val goods: Goods? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDeleted: Boolean = false
) 