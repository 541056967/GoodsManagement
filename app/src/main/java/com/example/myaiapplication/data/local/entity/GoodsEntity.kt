package com.example.myaiapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "goods")
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val brand: String?,
    val category: String,
    val status: String,
    val createTime: Date,
    val updateTime: Date
) 