package com.example.demoworkservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            setOneTimeRequest()
            createNotificationChannel()
            lanzaNotificacion("Hola","Mundo")
        }


    }

    private fun setOneTimeRequest() {
        val wm: WorkManager = WorkManager.getInstance(applicationContext)
        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val misDatos: Data = Data.Builder().putInt("key1", 45).build()

        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(misDatos)
                .build()

        wm.enqueue(uploadRequest)
        wm.getWorkInfoById(uploadRequest.id)
        wm.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            findViewById<TextView>(R.id.textview).text = it.state.name
        })
    }

    private fun createNotificationChannel() {
        val nombre = "Chafa Noticias"
        val descripcion = "Las noticias más reales del mundo."
        val importancia = NotificationManager.IMPORTANCE_DEFAULT
        val canal = NotificationChannel("CHAFA", nombre, importancia).apply{
            description = descripcion
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(canal)
    }

    private fun lanzaNotificacion(titulo: String, cuerpo: String){
        val intent = Intent(this, NotificationsListener::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(this, 0, intent, 0)


        val constructor = NotificationCompat.Builder(this, "CHAFA")
            .setSmallIcon(R.drawable.small_icon_foregroundt)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, constructor.build())
        }

    }
}