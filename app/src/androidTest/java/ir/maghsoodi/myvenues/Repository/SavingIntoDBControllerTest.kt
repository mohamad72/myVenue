package ir.maghsoodi.myvenues.Repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import ir.maghsoodi.myvenues.data.db.MyVenuesDataBase
import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.getOrAwaitValue
import ir.maghsoodi.myvenues.utils.Constants
import ir.maghsoodi.myvenues.utils.TimeManagementDefault
import ir.maghsoodi.myvenues.utils.TimeManagementFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class SavingIntoDBControllerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MyVenuesDataBase
    private lateinit var dao: VenueDao

    private lateinit var savingIntoDBController: SavingIntoDBController

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyVenuesDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getVenueDao()

        savingIntoDBController = SavingIntoDBController(dao, TimeManagementFake())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveMetaEntityIntoDB() = runBlockingTest {
        val metadata = MetaEntity("something", 200, 98.765, 12.345, 0L)
        savingIntoDBController.saveMetaEntityIntoDB(metadata)

        val allMetaEntity = dao.getAllMetaCalls().getOrAwaitValue()

        Truth.assertThat(allMetaEntity.get(0).created_at).isEqualTo(Constants.FAKE_CURRENT_TIME)
    }

}