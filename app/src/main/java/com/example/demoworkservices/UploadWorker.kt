package com.example.demoworkservices

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(context: Context, workerParameters: WorkerParameters):
    Worker(context, workerParameters) {

    //private val actividad = MainActivity()

    override fun doWork(): Result {
        try {
            val miVar = inputData.getString(MainActivity.KEY_VALUE)
            Log.i("WorkerTag", "Trabaja el ${this.id}")
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
