package ir.maghsoodi.myvenues.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.maghsoodi.myvenues.data.models.Category
import ir.maghsoodi.myvenues.data.models.Contact
import ir.maghsoodi.myvenues.data.models.Location
import java.lang.reflect.Type

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
    fun fromCategories(categories: List<Category>): String {
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun toCategories(categoriesString: String): List<Category> {
        val userListType: Type = object : TypeToken<ArrayList<Category?>?>() {}.getType()
        return Gson().fromJson(categoriesString, userListType)
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