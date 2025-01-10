package com.example.myaiapplication.ui.screens.goods.edit

import com.example.myaiapplication.domain.model.GoodsStatus
import com.example.myaiapplication.domain.model.Location
import com.example.myaiapplication.domain.model.PurchaseInfo

data class GoodsEditState(
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val category: String = "",
    val status: GoodsStatus = GoodsStatus.NORMAL,
    val location: Location = Location("", "", null),
    val purchaseInfo: PurchaseInfo = PurchaseInfo(null, "", "", null),
    val tags: List<String> = emptyList(),
    val attributes: Map<String, String> = emptyMap(),
    val photoUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
) 