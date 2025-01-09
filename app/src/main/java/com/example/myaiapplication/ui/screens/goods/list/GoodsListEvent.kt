package com.example.myaiapplication.ui.screens.goods.list

sealed class GoodsListEvent {
    object Refresh : GoodsListEvent()
    data class SearchQueryChanged(val query: String) : GoodsListEvent()
    data class CategorySelected(val category: String) : GoodsListEvent()
    data class AreaSelected(val areaId: String) : GoodsListEvent()
} 