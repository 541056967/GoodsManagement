package com.example.myaiapplication.ui.screens.goods.edit

import com.example.myaiapplication.domain.model.GoodsStatus
import com.example.myaiapplication.domain.model.Location
import com.example.myaiapplication.domain.model.PurchaseInfo

sealed class GoodsEditEvent {
    data class NameChanged(val name: String) : GoodsEditEvent()
    data class DescriptionChanged(val description: String) : GoodsEditEvent()
    data class BrandChanged(val brand: String) : GoodsEditEvent()
    data class CategoryChanged(val category: String) : GoodsEditEvent()
    data class StatusChanged(val status: GoodsStatus) : GoodsEditEvent()
    data class LocationChanged(val location: Location) : GoodsEditEvent()
    data class PurchaseInfoChanged(val purchaseInfo: PurchaseInfo?) : GoodsEditEvent()
    data class TagAdded(val tag: String) : GoodsEditEvent()
    data class TagRemoved(val tag: String) : GoodsEditEvent()
    data class AttributeAdded(val key: String, val value: String) : GoodsEditEvent()
    data class AttributeRemoved(val key: String) : GoodsEditEvent()
    data class PhotoAdded(val url: String) : GoodsEditEvent()
    data class PhotoRemoved(val url: String) : GoodsEditEvent()
    data class PhotosChanged(val photoUrls: List<String>) : GoodsEditEvent()
    object Save : GoodsEditEvent()
} 