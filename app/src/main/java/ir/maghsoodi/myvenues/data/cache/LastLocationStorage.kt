package ir.maghsoodi.myvenues.data.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.PreferencesProto
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import ir.maghsoodi.myvenues.utils.Constants
import ir.maghsoodi.myvenues.utils.Constants.Companion.LOCATION_LAT_CAFE_BAZAAR
import ir.maghsoodi.myvenues.utils.Constants.Companion.LOCATION_LNG_CAFE_BAZAAR
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LastLocationStorage @Inject constructor(
    val dataStore: DataStore<Preferences>
){

    public suspend fun saveLat(value: Double) {
        val dataStoreKey = doublePreferencesKey("lat")
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    public suspend fun saveLng(value: Double) {
        val dataStoreKey = doublePreferencesKey("lng")
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    public suspend fun getLat(): Double {
        val dataStoreKey = doublePreferencesKey("lat")
        val preferences = dataStore.data.first()
        if(preferences.contains(dataStoreKey))
            return preferences[dataStoreKey]!!
        return LOCATION_LAT_CAFE_BAZAAR
    }

    public suspend fun getLng(): Double {
        val dataStoreKey = doublePreferencesKey("lng")
        val preferences = dataStore.data.first()
        if(preferences.contains(dataStoreKey))
            return preferences[dataStoreKey]!!
        return LOCATION_LNG_CAFE_BAZAAR
    }
}