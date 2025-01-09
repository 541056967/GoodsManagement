package com.example.myaiapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Transaction
    @Query("SELECT DISTINCT areaId FROM location")
    fun getAllDistinctAreaIds(): Flow<List<String>>

}