package ir.maghsoodi.myvenues.utils

class TimeManagementDefault : TimeManagement {
    override fun getCurrentUnixTime(): Long {
        return System.currentTimeMillis()
    }
}