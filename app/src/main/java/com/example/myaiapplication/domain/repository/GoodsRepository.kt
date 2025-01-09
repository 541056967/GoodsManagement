package com.example.myaiapplication.domain.repository

import com.example.myaiapplication.domain.model.Goods
import kotlinx.coroutines.flow.Flow

interface GoodsRepository {
    // 基本操作
    suspend fun insertGoods(goods: Goods): Long
    suspend fun updateGoods(goods: Goods)
    suspend fun deleteGoods(goodsId: Long)

    // 查询操作
    fun getGoodsById(goodsId: Long): Flow<Goods?>
    fun getAllGoods(): Flow<List<Goods>>
    fun getGoodsByCategory(category: String): Flow<List<Goods>>
    fun getGoodsByArea(areaId: String): Flow<List<Goods>>

    fun searchGoods(query: String): Flow<List<Goods>>

    // 统计操作
    fun getGoodsCount(): Flow<Int>
    fun getTotalValue(): Flow<Double?>

    fun getAllLocations(): Flow<List<String>>
} 