package com.example.myaiapplication.data.mapper

import com.example.myaiapplication.data.local.entity.*
import com.example.myaiapplication.domain.model.*

fun GoodsWithRelations.toGoods(): Goods {
    return Goods(
        id = goods.id,
        name = goods.name,
        description = goods.description,
        brand = goods.brand,
        category = goods.category,
        tags = tags?.map { it.tag } ?: emptyList(),
        attributes = attributes?.associate { it.key to it.value } ?: emptyMap(),
        purchaseInfo = purchaseInfo?.toPurchaseInfo(),
        location = location?.toLocation() ?: Location("", "", null),
        photoUrls = photos?.map { it.url } ?: emptyList(),
        status = GoodsStatus.valueOf(goods.status),
        createTime = goods.createTime,
        updateTime = goods.updateTime
    )
}

fun Goods.toGoodsEntity(): GoodsEntity {
    return GoodsEntity(
        id = id,
        name = name,
        description = description,
        brand = brand,
        category = category,
        status = status.name,
        createTime = createTime,
        updateTime = updateTime
    )
}

fun PurchaseInfoEntity.toPurchaseInfo(): PurchaseInfo {
    return PurchaseInfo(
        date = date,
        purchasePrice = purchasePrice,
        currentMarketPrice = currentMarketPrice,
        channel = channel
    )
}

fun PurchaseInfo.toPurchaseInfoEntity(goodsId: Long): PurchaseInfoEntity {
    return PurchaseInfoEntity(
        goodsId = goodsId,
        date = date,
        purchasePrice = purchasePrice,
        currentMarketPrice = currentMarketPrice,
        channel = channel
    )
}

fun LocationEntity.toLocation(): Location {
    return Location(
        areaId = areaId,
        containerPath = containerPath,
        detail = detail,
        photos = photos
    )
}

fun Location.toLocationEntity(goodsId: Long): LocationEntity {
    return LocationEntity(
        goodsId = goodsId,
        areaId = areaId,
        containerPath = containerPath,
        detail = detail,
        photos = photos
    )
} 