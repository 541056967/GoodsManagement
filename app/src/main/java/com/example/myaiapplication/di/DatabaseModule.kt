package com.example.myaiapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myaiapplication.data.local.dao.GoodsDao
import com.example.myaiapplication.data.local.database.GoodsDatabase
import com.example.myaiapplication.data.repository.GoodsRepositoryImpl
import com.example.myaiapplication.domain.repository.GoodsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGoodsDatabase(
        @ApplicationContext context: Context
    ): GoodsDatabase {
        return Room.databaseBuilder(
            context,
            GoodsDatabase::class.java,
            "goods.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGoodsDao(database: GoodsDatabase): GoodsDao {
        return database.goodsDao()
    }

    @Provides
    @Singleton
    fun provideGoodsRepository(
        goodsDao: GoodsDao
    ): GoodsRepository {
        return GoodsRepositoryImpl(goodsDao)
    }
} 