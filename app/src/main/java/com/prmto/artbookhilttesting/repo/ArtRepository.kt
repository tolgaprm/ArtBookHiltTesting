package com.prmto.artbookhilttesting.repo

import androidx.lifecycle.LiveData
import com.prmto.artbookhilttesting.api.RetrofitAPI
import com.prmto.artbookhilttesting.model.ImageResponse
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.roomdb.ArtDao
import com.prmto.artbookhilttesting.util.Resource
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI
) : ArtRepositoryInterface {

    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.delete(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(searchQuery: String): Resource<ImageResponse> {
        return try {
            val response = retrofitAPI.imageSearch(searchQuery = searchQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("Error", null)
            } else {
                Resource.Error("Error", null)
            }
        } catch (e: Exception) {
            Resource.Error("no Data", null)
        }
    }
}