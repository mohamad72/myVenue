package ir.maghsoodi.myvenues.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.Relations.MetaWithVenues
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Constants.Companion.MINIMUM_EXPIRE_TIME

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenue(venueEntity: VenueEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeta(metaEntity: MetaEntity)

    @Query("SELECT * FROM metaentity Where created_at >= :minimumTimeValid ORDER BY created_at DESC")
    suspend fun getAllMetaCalls(minimumTimeValid: Long = MINIMUM_EXPIRE_TIME): List<MetaEntity>

    @Transaction
    @Query("SELECT * FROM metaentity WHERE requestId = :requestId")
    suspend fun getMetaWithVenues(requestId: String): List<MetaWithVenues>

    @Query("DELETE FROM metaentity WHERE created_at < :minimumTimeValid")
    suspend fun deleteOldMeta(minimumTimeValid: Long = MINIMUM_EXPIRE_TIME)

    @Delete
    suspend fun deleteVenue(venueEntity: VenueEntity)


}