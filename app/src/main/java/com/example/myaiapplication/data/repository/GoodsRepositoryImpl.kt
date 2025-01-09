package com.example.myaiapplication.data.repository

import com.example.myaiapplication.data.local.dao.GoodsDao
import com.example.myaiapplication.data.local.dao.LocationDao
import com.example.myaiapplication.data.local.entity.*
import com.example.myaiapplication.data.mapper.toGoods
import com.example.myaiapplication.data.mapper.toGoodsEntity
import com.example.myaiapplication.data.mapper.toLocationEntity
import com.example.myaiapplication.data.mapper.toPurchaseInfoEntity
import com.example.myaiapplication.domain.model.Goods
import com.example.myaiapplication.domain.repository.GoodsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoodsRepositoryImpl @Inject constructor(
    private val goodsDao: GoodsDao,
    private val locationDao: LocationDao
) : GoodsRepository {

    override suspend fun insertGoods(goods: Goods): Long {
        val goodsId = goodsDao.insertGoods(goods.toGoodsEntity())
        
        // 插入关联数据
        goods.purchaseInfo?.let { 
            goodsDao.insertPurchaseInfo(it.toPurchaseInfoEntity(goodsId))
        }
        goodsDao.insertLocation(goods.location.toLocationEntity(goodsId))
        if (goods.tags.isNotEmpty()) {
            goodsDao.insertTags(goods.tags.map { GoodsTagEntity(goodsId, it) })
        }
        if (goods.attributes.isNotEmpty()) {
            goodsDao.insertAttributes(goods.attributes.map { 
                GoodsAttributeEntity(goodsId, it.key, it.value) 
            })
        }
        if (goods.photoUrls.isNotEmpty()) {
            goodsDao.insertPhotos(goods.photoUrls.map { 
                PhotoEntity(goodsId, it, goods.createTime) 
            })
        }
        
        return goodsId
    }

    override suspend fun updateGoods(goods: Goods) {
        goodsDao.updateGoods(goods.toGoodsEntity())
        
        // 更新关联数据前先删除旧数据
        goodsDao.deleteGoodsTags(goods.id)
        goodsDao.deleteGoodsAttributes(goods.id)
        goodsDao.deleteGoodsPhotos(goods.id)
        
        // 插入新的关联数据
        goods.purchaseInfo?.let { 
            goodsDao.insertPurchaseInfo(it.toPurchaseInfoEntity(goods.id))
        }
        goodsDao.insertLocation(goods.location.toLocationEntity(goods.id))
        if (goods.tags.isNotEmpty()) {
            goodsDao.insertTags(goods.tags.map { GoodsTagEntity(goods.id, it) })
        }
        if (goods.attributes.isNotEmpty()) {
            goodsDao.insertAttributes(goods.attributes.map { 
                GoodsAttributeEntity(goods.id, it.key, it.value) 
            })
        }
        if (goods.photoUrls.isNotEmpty()) {
            goodsDao.insertPhotos(goods.photoUrls.map { 
                PhotoEntity(goods.id, it, goods.createTime) 
            })
        }
    }

    override suspend fun deleteGoods(goodsId: Long) {
        // 关联数据会通过外键级联删除
        goodsDao.deleteGoods(goodsId)
    }

    override fun getGoodsById(goodsId: Long): Flow<Goods?> {
        return goodsDao.getGoodsWithRelationsById(goodsId)
            .map { it?.toGoods() }
    }

    override fun getAllGoods(): Flow<List<Goods>> {
        return goodsDao.getAllGoodsWithRelations()
            .map { list -> list.map { it.toGoods() } }
    }

    override fun getGoodsByCategory(category: String): Flow<List<Goods>> {
        return goodsDao.getGoodsByCategory(category)
            .map { list -> list.map { it.toGoods() } }
    }

    override fun getGoodsByArea(areaId: String): Flow<List<Goods>> {
        return goodsDao.getGoodsByAreaId(areaId)
            .map { list -> list.map { it.toGoods() } }
    }

    override fun searchGoods(query: String): Flow<List<Goods>> {
        return goodsDao.searchGoods(query)
            .map { list -> list.map { it.toGoods() } }
    }

    override fun getGoodsCount(): Flow<Int> {
        return goodsDao.getGoodsCount()
    }

    override fun getTotalValue(): Flow<Double?> {
        return goodsDao.getTotalValue()
    }

    override fun getAllLocations(): Flow<List<String>> {
        return locationDao.getAllDistinctAreaIds()
    }
} 