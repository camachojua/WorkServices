package com.example.demoworkservices

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(context: Context, workerParameters: WorkerParameters):
    Worker(context, workerParameters) {

    private var miVar = inputData.getString(MainActivity.KEY_VALUE) ?: "No hay mensaje :("

    override fun doWork(): Result {
        try {
            Log.i("WorkerTag", "Trabaja el ${this.id}")
            Log.i("WorkerTag", miVar)

            var builder = NotificationCompat.Builder(applicationContext, "CHAFA")
                    .setSmallIcon(R.drawable.small_icon_foreground)
                    .setContentTitle("WORKERS")
                    .setContentText(miVar)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(1, builder.build())
            }

            return Result.success()
        } catch (ex: Exception) {
            Log.e("WorkerManagerTag", ex.toString())
            return Result.retry()
        }
    }
}

private fun Data.getInt(data: String): Int {
    return data.toInt()
}
