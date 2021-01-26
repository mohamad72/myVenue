package ir.maghsoodi.myvenues.Repository

import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.TimeManagement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.CacheRequest
import javax.inject.Inject


class SavingIntoDBController @Inject constructor(
    val venueDao: VenueDao,
    val timeManagement: TimeManagement
) {

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity) {

    }

    suspend fun saveVenueEntityIntoDB(requestId: Int, venueEntities: List<VenueEntity>) {

    }
}