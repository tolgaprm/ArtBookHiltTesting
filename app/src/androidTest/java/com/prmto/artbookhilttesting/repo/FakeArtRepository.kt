package com.prmto.artbookhilttesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prmto.artbookhilttesting.model.ImageResponse
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.util.Resource

class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(searchQuery: String): Resource<ImageResponse> {
        return Resource.Success(ImageResponse(listOf(), 0, 0))
    }

    private fun refreshData() {
        artsLiveData.postValue(arts)
    }
}