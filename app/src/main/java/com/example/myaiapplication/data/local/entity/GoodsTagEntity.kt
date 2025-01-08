package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "goods_tags",
    primaryKeys = ["goodsId", "tag"],
    foreignKeys = [
        ForeignKey(
            entity = GoodsEntity::class,
            parentColumns = ["id"],
            childColumns = ["goodsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GoodsTagEntity(
    val goodsId: Long,
    val tag: String
) 