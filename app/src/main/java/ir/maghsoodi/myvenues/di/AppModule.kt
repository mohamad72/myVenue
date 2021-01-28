package ir.maghsoodi.myvenues.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ir.maghsoodi.myvenues.main.DBController
import ir.maghsoodi.myvenues.data.db.MyVenuesDataBase
import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.ApiFoursquare
import ir.maghsoodi.myvenues.main.FacadePatternRepository
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.Constants.Companion.BASE_URL
import ir.maghsoodi.myvenues.utils.Constants.Companion.DATABASE_NAME
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import ir.maghsoodi.myvenues.utils.TimeManagement
import ir.maghsoodi.myvenues.utils.TimeManagementDefault
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiFoursquare(): ApiFoursquare = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiFoursquare::class.java)

    @Singleton
    @Provides
    fun provideAppDb(app: Application): MyVenuesDataBase {
        return Room.databaseBuilder(app, MyVenuesDataBase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideVenueDao(db: MyVenuesDataBase): VenueDao {
        return db.getVenueDao()
    }

    @Singleton
    @Provides
    fun provideTimeManagement(): TimeManagement {
        return TimeManagementDefault()
    }

    @Singleton
    @Provides
    fun provideSavingIntoDBController(
        timeManagement: TimeManagement,
        venueDao: VenueDao
    ): DBController {
        return DBController(venueDao, timeManagement)
    }

    @Singleton
    @Provides
    fun provideMainRepository(
        facadeRepository: FacadePatternRepository,
        timeManagement: TimeManagement
    ):
            MainRepository = MainRepository(facadeRepository, timeManagement)


    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}