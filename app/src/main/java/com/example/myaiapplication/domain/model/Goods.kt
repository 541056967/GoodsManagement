package com.example.myaiapplication.domain.model

import java.util.Date

data class Goods(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val brand: String? = null,
    val category: String,
    val tags: List<String> = emptyList(),
    val attributes: Map<String, String> = emptyMap(),
    val purchaseInfo: PurchaseInfo,
    val location: Location,
    val photoUrls: List<String> = emptyList(),
    val status: GoodsStatus = GoodsStatus.NORMAL,
    val createTime: Date = Date(),
    val updateTime: Date = Date()
)

data class PurchaseInfo(
    val date: Date? = null,
    val purchasePrice: String,
    val currentMarketPrice: String,
    val channel: String? = null
)

data class Location(
    val areaId: String,
    val containerPath: String,
    val detail: String? = null,
    val photos: List<String> = emptyList()
)

enum class GoodsStatus {
    NORMAL,
    DISPOSED,
    DAMAGED
} 