package com.prmto.artbookhilttesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prmto.artbookhilttesting.model.ImageResponse
import com.prmto.artbookhilttesting.repo.ArtRepositoryInterface
import com.prmto.artbookhilttesting.roomdb.Art
import com.prmto.artbookhilttesting.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    private val repository: ArtRepositoryInterface
) : ViewModel() {

    // Art Fragment
    val artList = repository.getArt()

    // Image API Fragment
    private val _images = MutableLiveData<Resource<ImageResponse>>()
    val image: LiveData<Resource<ImageResponse>> get() = _images

    private val _selectedImage = MutableLiveData<String>()
    val selectedImage: LiveData<String> get() = _selectedImage

    // Arts Detail Fragment
    private var _insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMsg: LiveData<Resource<Art>> get() = _insertArtMsg

    fun resetInsertArtMsg() {
        _insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        _selectedImage.value = url
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun makeArt(name: String, artistName: String, year: String) {
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) {
            _insertArtMsg.value = Resource.Error("Enter name,artist,year")
            return
        }

        val yearInt = try {
            year.toInt()
        } catch (e: Exception) {
            _insertArtMsg.value = Resource.Error("Year should be number")
            return
        }

        val art =
            Art(
                name = name,
                artistName = artistName,
                year = yearInt,
                imageUrl = _selectedImage.value ?: ""
            )

        insertArt(art)
        setSelectedImage("")
        _insertArtMsg.value = Resource.Success(art)
    }

    fun searchForImage(search: String) {
        if (search.isEmpty()) return

        _images.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(search)
            _images.value = response
        }
    }

}