package com.example.myaiapplication.data.local.dao

import androidx.room.*
import com.example.myaiapplication.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GoodsDao {
    // 基本CRUD操作
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoods(goods: GoodsEntity): Long

    @Update
    suspend fun updateGoods(goods: GoodsEntity)

    @Query("DELETE FROM goods WHERE id = :goodsId")
    suspend fun deleteGoods(goodsId: Long)

    // 关联表操作
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseInfo(purchaseInfo: PurchaseInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<GoodsTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttributes(attributes: List<GoodsAttributeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    // 查询操作
    @Transaction
    @Query("SELECT * FROM goods WHERE id = :goodsId")
    fun getGoodsWithRelationsById(goodsId: Long): Flow<GoodsWithRelations?>

    @Transaction
    @Query("SELECT * FROM goods ORDER BY createTime DESC")
    fun getAllGoodsWithRelations(): Flow<List<GoodsWithRelations>>

    @Transaction
    @Query("SELECT * FROM goods WHERE category = :category")
    fun getGoodsByCategory(category: String): Flow<List<GoodsWithRelations>>

    @Transaction
    @Query("""
        SELECT DISTINCT g.* FROM goods g 
        LEFT JOIN goods_tags t ON g.id = t.goodsId 
        WHERE g.name LIKE '%' || :query || '%' 
        OR g.description LIKE '%' || :query || '%' 
        OR t.tag LIKE '%' || :query || '%'
    """)
    fun searchGoods(query: String): Flow<List<GoodsWithRelations>>

    // 删除关联数据
    @Query("DELETE FROM goods_tags WHERE goodsId = :goodsId")
    suspend fun deleteGoodsTags(goodsId: Long)

    @Query("DELETE FROM goods_attributes WHERE goodsId = :goodsId")
    suspend fun deleteGoodsAttributes(goodsId: Long)

    @Query("DELETE FROM photos WHERE goodsId = :goodsId")
    suspend fun deleteGoodsPhotos(goodsId: Long)

    // 统计查询
    @Query("SELECT COUNT(*) FROM goods")
    fun getGoodsCount(): Flow<Int>

    @Query("""
        SELECT SUM(CASE 
            WHEN currentMarketPrice IS NOT NULL THEN currentMarketPrice 
            ELSE purchasePrice 
        END) FROM purchase_info
    """)
    fun getTotalValue(): Flow<Double?>
} 