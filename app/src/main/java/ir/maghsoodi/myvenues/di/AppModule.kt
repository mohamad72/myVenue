package ir.maghsoodi.myvenues.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ir.maghsoodi.myvenues.Repository.SavingIntoDBController
import ir.maghsoodi.myvenues.data.db.MyVenuesDataBase
import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.ApiFoursquare
import ir.maghsoodi.myvenues.utils.Constants
import ir.maghsoodi.myvenues.utils.Constants.Companion.BASE_URL
import ir.maghsoodi.myvenues.utils.Constants.Companion.DATABASE_NAME
import ir.maghsoodi.myvenues.utils.TimeManagement
import ir.maghsoodi.myvenues.utils.TimeManagementDefault
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
    ): SavingIntoDBController {
        return SavingIntoDBController(venueDao, timeManagement)
    }
}