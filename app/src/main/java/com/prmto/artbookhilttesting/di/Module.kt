package com.prmto.artbookhilttesting.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.prmto.artbookhilttesting.R
import com.prmto.artbookhilttesting.api.RetrofitAPI
import com.prmto.artbookhilttesting.repo.ArtRepository
import com.prmto.artbookhilttesting.repo.ArtRepositoryInterface
import com.prmto.artbookhilttesting.roomdb.ArtDao
import com.prmto.artbookhilttesting.roomdb.ArtDatabase
import com.prmto.artbookhilttesting.util.Util
import com.prmto.artbookhilttesting.viewmodel.ArtViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun injectRetrofit(): RetrofitAPI {
        return Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context): ArtDatabase {
        return Room.databaseBuilder(context, ArtDatabase::class.java, "artBookDB")
            .build()
    }

    @Singleton
    @Provides
    fun injectRoom(database: ArtDatabase) = database.artDao

    @Singleton
    @Provides
    fun injectGlide(application: Application): RequestManager {
        return Glide.with(application.applicationContext)
            .setDefaultRequestOptions(
                RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
            )
    }

    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, retrofitAPI: RetrofitAPI): ArtRepositoryInterface {
        return ArtRepository(dao, retrofitAPI)
    }

}
