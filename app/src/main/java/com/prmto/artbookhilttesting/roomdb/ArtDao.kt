package com.prmto.artbookhilttesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Delete
    suspend fun delete(art: Art)


    // BUrada livedata yazdığımız içim suspend yazmıyoruz. Livedata kendisi zaten async çalışır.
    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>


}