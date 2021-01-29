package ir.maghsoodi.myvenues.ui

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ir.maghsoodi.myvenues.utils.Constants.Companion.INTERNET_IS_ONLINE_MESSAGE


class MyWork(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        sendConnectionState()
        return Result.success()
    }

    private fun sendConnectionState() {
        val intent = Intent(INTERNET_IS_ONLINE_MESSAGE)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }
}