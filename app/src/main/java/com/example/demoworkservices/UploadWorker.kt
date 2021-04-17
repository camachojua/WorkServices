package com.example.demoworkservices

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(context: Context, workerParameters: WorkerParameters):
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        try {
            val miVar = inputData.getInt(MainActivity.KEY_VALUE, 0)

            for (i: Int in 0..600) {
                Log.i("WorkerManagerTag","Subiendo la bilirrubina $i")
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
