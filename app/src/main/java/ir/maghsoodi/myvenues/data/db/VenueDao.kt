package ir.maghsoodi.myvenues.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.Relations.MetaWithVenues
import ir.maghsoodi.myvenues.data.models.VenueEntity

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenue(venueEntity: VenueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeta(metaEntity: MetaEntity)

    @Query("SELECT * FROM metaentity ORDER BY created_at DESC")
    fun getAllMetaCalls(): LiveData<List<MetaEntity>>

    @Transaction
    @Query("SELECT * FROM metaentity WHERE requestId = :requestId")
    suspend fun getMetaWithVenues(requestId: String): List<MetaWithVenues>

    @Delete
    suspend fun deleteMeta(metaEntity: MetaEntity)

    @Delete
    suspend fun deleteVenue(venueEntity: VenueEntity)


}