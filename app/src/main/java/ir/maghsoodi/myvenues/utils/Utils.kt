package ir.maghsoodi.myvenues.utils

import android.location.Location

class Utils {
    companion object {
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


    }
}