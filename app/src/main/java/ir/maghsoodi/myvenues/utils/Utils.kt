package ir.maghsoodi.myvenues.utils

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import ir.maghsoodi.myvenues.R
import pub.devrel.easypermissions.EasyPermissions


object Utils {
    fun calculateDistance(
        firstLat: Double,
        firstLng: Double,
        secondLat: Double,
        secondLng: Double
    ): Int {
        val results = FloatArray(1)
        Location.distanceBetween(
            firstLat, firstLng,
            secondLat, secondLng, results
        )
        return results[0].toInt()
    }


    fun getImage(categoryName: String): Int {
        return when (categoryName) {
            "Bridge" -> R.drawable.bridge
            "Hookah Bar", "Restaurant", "Asian Restaurant", "BBQ Joint", "Italian Restaurant", "Juice Bar", "Tabbakhi", "Persian Restaurant" -> R.drawable.restaurant
            "Bank" -> R.drawable.bank
            "Bakery" -> R.drawable.bakery
            "College Bookstore", "Bookstore" -> R.drawable.book_shop
            "Airport" -> R.drawable.airport
            "Sandwich Place", "Fast Food Restaurant" -> R.drawable.pizza_shop
            "Voting Booth" -> R.drawable.church
            "Plaza" -> R.drawable.castle
            "Tea Room", "Lounge", "Café" -> R.drawable.coffee_shop
            "College Library" -> R.drawable.place_library
            "Auto Dealership", "Auto Garage", "Car Wash" -> R.drawable.parking
            "Gym / Fitness Center", "Gyms or Fitness Centers", "Gym", "College Gym" -> R.drawable.gym
            "Doctor's Office", "Medical Center", "Health & Beauty Service" -> R.drawable.hospital
            "Wedding Hall", "Hotel" -> R.drawable.hotel
            "Bus Line", "Bus Station" -> R.drawable.bus_station
            "Shopping Mall", "Smoke Shop", "Gym Pools", "Market", "Supermarket" -> R.drawable.supermarket
            "Drugstore" -> R.drawable.pharmacy
            "Amphitheater" -> R.drawable.theater
            "Park" -> R.drawable.park
            "Music School", "School", "Student Center" -> R.drawable.school
            "College Auditorium", "College Academic Building", "General College & University" -> R.drawable.university
            "Garden" -> R.drawable.zoo
            "Soccer Field" -> R.drawable.stadium
            "Government Building", "Business Center", "Campaign Office", "Office" -> R.drawable.office
            "Military Base" -> R.drawable.police_station
            "Eye Doctor", "Dentist's Office" -> R.drawable.clinic
            else -> R.drawable.office
        }
    }

    fun hasLocationPermission(context: Context): Boolean = EasyPermissions.hasPermissions(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun isGPSEnable(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun hasInternetConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}