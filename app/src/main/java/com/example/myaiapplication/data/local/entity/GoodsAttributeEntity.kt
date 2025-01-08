package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "goods_attributes",
    primaryKeys = ["goodsId", "key"],
    foreignKeys = [
        ForeignKey(
            entity = GoodsEntity::class,
            parentColumns = ["id"],
            childColumns = ["goodsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GoodsAttributeEntity(
    val goodsId: Long,
    val key: String,
    val value: String
) 