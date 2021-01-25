package ir.maghsoodi.myvenues.data.db

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

    @Transaction
    @Query("SELECT * FROM meta_table WHERE requestId = :requestId")
    suspend fun getSchoolWithStudents(requestId: String): List<MetaWithVenues>

}