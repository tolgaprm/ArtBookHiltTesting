package com.prmto.artbookhilttesting.di

import android.content.Context
import androidx.room.Room
import com.prmto.artbookhilttesting.roomdb.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("testDatabase") // Bu işlemi asıl inject yaptığım dataabseden almaması için yapıyorun
    // Çünkü bu inMemoryDatabase ile oluşturulmalı
    fun injectInMemoryRoom(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ArtDatabase::class.java)
            .allowMainThreadQueries().build()

}