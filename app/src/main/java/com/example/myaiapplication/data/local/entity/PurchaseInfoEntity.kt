package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "purchase_info",
    foreignKeys = [
        ForeignKey(
            entity = GoodsEntity::class,
            parentColumns = ["id"],
            childColumns = ["goodsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PurchaseInfoEntity(
    @PrimaryKey
    val goodsId: Long,
    val date: Date?,
    val purchasePrice: Double?,
    val currentMarketPrice: Double?,
    val channel: String?
) 