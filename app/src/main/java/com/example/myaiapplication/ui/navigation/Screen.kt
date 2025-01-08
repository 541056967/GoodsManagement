package com.example.myaiapplication.ui.navigation

sealed class Screen(val route: String) {
    object GoodsList : Screen("goods_list")
    
    object GoodsDetail : Screen("goods_detail/{goodsId}") {
        fun createRoute(goodsId: Long) = "goods_detail/$goodsId"
    }
    
    object GoodsEdit : Screen("goods_edit?goodsId={goodsId}") {
        fun createRoute(goodsId: Long) = "goods_edit?goodsId=$goodsId"
    }
} 