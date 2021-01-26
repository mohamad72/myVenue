package ir.maghsoodi.myvenues.data.db

import ir.maghsoodi.myvenues.data.models.Location
import androidx.room.TypeConverter
import com.google.gson.Gson
import ir.maghsoodi.myvenues.data.models.Category
import ir.maghsoodi.myvenues.data.models.Contact

class TypeConverter {
    @TypeConverter
    fun fromLocation(location: Location): String {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        return Gson().fromJson(locationString, Location::class.java)
    }

    @TypeConverter
    fun fromContact(contact: Contact): String {
        return Gson().toJson(contact)
    }

    @TypeConverter
    fun toContact(contactString: String): Contact {
        return Gson().fromJson(contactString, Contact::class.java)
    }


}