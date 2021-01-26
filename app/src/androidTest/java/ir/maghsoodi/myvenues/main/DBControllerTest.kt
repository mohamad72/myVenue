package ir.maghsoodi.myvenues.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import ir.maghsoodi.myvenues.data.db.MyVenuesDataBase
import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.Contact
import ir.maghsoodi.myvenues.data.models.Location
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Constants
import ir.maghsoodi.myvenues.utils.TimeManagementFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DBControllerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MyVenuesDataBase
    private lateinit var dao: VenueDao

    private lateinit var DBController: DBController

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyVenuesDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getVenueDao()

        DBController = DBController(dao, TimeManagementFake())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveMetaEntityIntoDB() = runBlockingTest {
        val metadata = MetaEntity("something", 200, 98.765, 12.345, 0L)
        DBController.saveMetaEntityIntoDB(metadata)

        val allMetaEntity = dao.getAllMetaCalls()

        Truth.assertThat(allMetaEntity.get(0).created_at).isEqualTo(Constants.FAKE_CURRENT_TIME)
    }


    @Test
    fun saveVenueEntityIntoDB() = runBlockingTest {
        val metadata = MetaEntity("so1", 200, 98.765, 12.345, 0L)
        DBController.saveMetaEntityIntoDB(metadata)


        val entitiesList = ArrayList<VenueEntity>()
        entitiesList.add(
            VenueEntity(
                "something1",
                "",
                Contact(),
                Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
                "خونه"
            )
        )
        entitiesList.add(
            VenueEntity(
                "something2",
                "",
                Contact(),
                Location("tt", "tt", "tt", "tt", 500, 32.24, 54.23, "888"),
                "خوdfvfdvdfنه"
            )
        )

        DBController.saveVenueEntityIntoDB("so1", venueEntities = entitiesList)

        val allMetaEntity = dao.getMetaWithVenues("so1")

        Truth.assertThat(allMetaEntity.get(0).venues.get(0).requestId).isEqualTo("so1")
    }
}