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
    val purchaseInfo: PurchaseInfo? = null,
    val location: Location,
    val photoUrls: List<String> = emptyList(),
    val status: GoodsStatus = GoodsStatus.NORMAL,
    val createTime: Date = Date(),
    val updateTime: Date = Date()
)

data class PurchaseInfo(
    val date: Date,
    val purchasePrice: Double,
    val currentMarketPrice: Double? = null,
    val channel: String
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