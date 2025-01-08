package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "location",
    foreignKeys = [
        ForeignKey(
            entity = GoodsEntity::class,
            parentColumns = ["id"],
            childColumns = ["goodsId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocationEntity(
    @PrimaryKey
    val goodsId: Long,
    val areaId: String,
    val containerPath: String,
    val detail: String?,
    val photos: List<String>
) 