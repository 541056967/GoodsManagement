package com.example.myaiapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myaiapplication.data.local.converter.Converters
import com.example.myaiapplication.data.local.dao.GoodsDao
import com.example.myaiapplication.data.local.dao.LocationDao
import com.example.myaiapplication.data.local.entity.*

@Database(
    entities = [
        GoodsEntity::class,
        PurchaseInfoEntity::class,
        LocationEntity::class,
        GoodsTagEntity::class,
        GoodsAttributeEntity::class,
        PhotoEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GoodsDatabase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao

    abstract fun locationDao(): LocationDao
} 