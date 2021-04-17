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
            val miVar = inputData.getInt("key1")

            for (i: Int in 0..600) {
                Log.i("WorkerManagerTag","Subiendo la bilirrubina $i")
            }
        } catch (ex: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}

private fun Data.getInt(data: String): Int {
    return data.toInt()
}
