package com.example.myaiapplication.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GoodsWithRelations(
    @Embedded
    val goods: GoodsEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "goodsId"
    )
    val purchaseInfo: PurchaseInfoEntity?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "goodsId"
    )
    val location: LocationEntity?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "goodsId"
    )
    val tags: List<GoodsTagEntity>?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "goodsId"
    )
    val attributes: List<GoodsAttributeEntity>?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "goodsId"
    )
    val photos: List<PhotoEntity>?
) 