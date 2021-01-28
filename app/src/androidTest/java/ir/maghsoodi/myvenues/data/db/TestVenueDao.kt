package ir.maghsoodi.myvenues.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import ir.maghsoodi.myvenues.data.models.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Categories
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TestVenueDao {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MyVenuesDataBase
    private lateinit var dao: VenueDao

    private lateinit var categories: ArrayList<Category>

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyVenuesDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getVenueDao()

        categories=ArrayList()
        categories.add(Category(""))
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertMeta() = runBlockingTest {
        val metadata = MetaEntity("something", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata)

        val allMetaEntity = dao.getAllMetaCalls()

        assertThat(allMetaEntity).contains(metadata)
    }

    @Test
    fun testReplaceNewDataWithOldDataInMetaDataBase() = runBlockingTest {
        val metadata1 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata2 = MetaEntity("something2", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata3 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata4 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata5 = MetaEntity("something1", 200, 98.765, 15.345, System.currentTimeMillis(),"")

        dao.insertMeta(metadata1)
        dao.insertMeta(metadata2)
        dao.insertMeta(metadata3)
        dao.insertMeta(metadata4)
        dao.insertMeta(metadata5)

        val allMetaEntity = dao.getAllMetaCalls()

        assertThat(allMetaEntity.size).isEqualTo(2)
    }

    @Test
    fun testReplaceNewDataWithOldDataAndJoinDataBaseCorrectAgain() = runBlockingTest {
        val metadata1 = MetaEntity("so1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata1)
        val metadata2 = MetaEntity("so2", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata2)

        val venueEntity1 = VenueEntity(
            "something1",
            "so1",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )
        val venueEntity2 = VenueEntity(
            "something2",
            "so2",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )
        val venueEntity3 = VenueEntity(
            "something1",
            "so2",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )

        dao.insertVenue(venueEntity1)
        dao.insertVenue(venueEntity2)
        dao.insertVenue(venueEntity3)



        val allVenueEntity2 = dao.getMetaWithVenues("so2")

        assertThat(allVenueEntity2.get(0).venues.size).isEqualTo(2)

        val allVenueEntity1 = dao.getMetaWithVenues("so1")

        assertThat(allVenueEntity1.get(0).venues.size).isEqualTo(0)
    }

    @Test
    fun testDeleteExpiredMeta() = runBlockingTest {
        val metadata1 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata2 = MetaEntity("something2", 200, 98.765, 12.345, 16L,"")
        val metadata3 = MetaEntity("something3", 200, 98.765, 12.345, System.currentTimeMillis(),"")

        dao.insertMeta(metadata1)
        dao.insertMeta(metadata2)
        dao.insertMeta(metadata3)

        dao.deleteOldMeta()

        val allMetaEntity = dao.getAllMetaCalls()

        assertThat(allMetaEntity).contains(metadata1)
        assertThat(allMetaEntity).doesNotContain(metadata2)
        assertThat(allMetaEntity).contains(metadata3)
    }

    @Test
    fun testGetJustNewMetaDataLiveData() = runBlockingTest {
        val metadata1 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        val metadata2 = MetaEntity("something2", 200, 98.765, 12.345, 16L,"")
        val metadata3 = MetaEntity("something3", 200, 98.765, 12.345, System.currentTimeMillis(),"")

        dao.insertMeta(metadata1)
        dao.insertMeta(metadata2)
        dao.insertMeta(metadata3)

        dao.deleteOldMeta()

        val allMetaEntity = dao.getAllMetaCalls()

        assertThat(allMetaEntity.size).isEqualTo(2)
    }

    @Test
    fun testJoinTable() = runBlockingTest {
        val metadata1 = MetaEntity("so1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata1)
        val metadata2 = MetaEntity("so2", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata2)


        val venueEntity1 = VenueEntity(
            "something1",
            "so1",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )
        val venueEntity2 = VenueEntity(
            "something2",
            "so2",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )
        val venueEntity3 = VenueEntity(
            "something3",
            "so1",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )

        dao.insertVenue(venueEntity1)
        dao.insertVenue(venueEntity2)
        dao.insertVenue(venueEntity3)

        val allVenueEntity = dao.getMetaWithVenues("so1")

        assertThat(allVenueEntity.size).isEqualTo(1)
        assertThat(allVenueEntity.get(0).venues.size).isEqualTo(2)
    }

    @Test
    fun testLocationConverter() = runBlockingTest {
        val metadata1 = MetaEntity("so1", 200, 98.765, 12.345, System.currentTimeMillis(),"")
        dao.insertMeta(metadata1)


        val venueEntity1 = VenueEntity(
            "something1",
            "so1",
            categories,
            Contact(),
            Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
            "خونه"
        )

        dao.insertVenue(venueEntity1)

        val allVenueEntity = dao.getMetaWithVenues("so1")

        assertThat(allVenueEntity.get(0).venues.get(0).location.lat).isEqualTo(32.24)
        assertThat(allVenueEntity.get(0).venues.get(0).location.lng).isEqualTo(54.23)
    }

}


