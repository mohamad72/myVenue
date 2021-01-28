package ir.maghsoodi.myvenues.main.facadePattern

import ir.maghsoodi.myvenues.data.models.*
import ir.maghsoodi.myvenues.utils.Resource
import timber.log.Timber
import java.util.ArrayList

class FacadePatternRepositoryFake :
    FacadePatternRepository {

    private var hasSuitableDataInDB = false

    fun hasSuitableDataInDB(value: Boolean) {
        hasSuitableDataInDB = value
    }

    private var isInternetEnable = false

    fun isInternetEnable(value: Boolean) {
        isInternetEnable = value
    }

    override suspend fun searchNearVenueFromRemote(
        lat: Double,
        lng: Double
    ): Resource<SearchResponse> {
        val metaEntity =
            MetaEntity("something", 200, 98.765, 12.345, System.currentTimeMillis(), "")
        val venueEntities = makeVenueEntityList()


        return try {
            if (isInternetEnable) {
                Resource.Success(SearchResponse(metaEntity, Response(venueEntities)))
            } else {
                Resource.Error("internet is disable")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }

    private fun makeVenueEntityList(): List<VenueEntity> {
        var categories: ArrayList<Category> = ArrayList()
        categories.add(Category(""))
        var venueEntities = ArrayList<VenueEntity>()
        repeat(15) {
            venueEntities.add(
                VenueEntity(
                    "id$it",
                    "so1",
                    categories,
                    Contact(),
                    Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
                    "حاج خلیفه رهبر"
                )
            )
        }

        return venueEntities
    }

    override suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity, lat: Double, lng: Double) {}

    override suspend fun saveVenueEntityIntoDB(
        metaEntity: MetaEntity,
        venueEntities: List<VenueEntity>
    ) {
    }

    override suspend fun hasNearestMetaInDB(lat: Double, lng: Double): Boolean {
        return hasSuitableDataInDB
    }

    override suspend fun getNearestMetaInDB(lat: Double, lng: Double): MetaEntity {
        return MetaEntity("something", 200, 98.765, 12.345, System.currentTimeMillis(), "")
    }

    override suspend fun getVenueEntitiesOfMetaFromDB(metaEntity: MetaEntity): List<VenueEntity> {
        return makeVenueEntityList()
    }

    override suspend fun deleteOldMeta(expireUnixTime: Long) {}


}