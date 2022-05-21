package com.prmto.artbookhilttesting.repo

import androidx.lifecycle.LiveData
import com.prmto.artbookhilttesting.model.ImageResponse
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(searchQuery: String): Resource<ImageResponse>
}

