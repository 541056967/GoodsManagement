package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "photos",
    primaryKeys = ["goodsId", "url"],
    foreignKeys = [
        ForeignKey(
            entity = GoodsEntity::class,
            parentColumns = ["id"],
            childColumns = ["goodsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhotoEntity(
    val goodsId: Long,
    val url: String,
    val createTime: Date
) 