package com.example.demoworkservices

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var tiempo : Int = 0;
    private var fecha: String = "";
    private var mensaje: String = "";
    companion object {
        const val KEY_VALUE = ""
        fun getInstance(): MainActivity? {
            return this.getInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            mensaje = (findViewById<TextInputLayout>(R.id.entradaUsuario)
                .editText?.text ?: "No hay mensaje :(") as String
            setOneTimeRequest()
            createNotificationChannel()
            // lanzaNotificacion("Hola",mensaje)
        }

        findViewById<TextView>(R.id.textoHora).setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                findViewById<TextView>(R.id.textoHora).text = h.toString() + ":" + m.toString()
                tiempo = m
            }),hour,minute,false) //

            tpd.show()
        }

        findViewById<TextView>(R.id.textoCalendario).setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val tpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener(function = { view, y, m,d ->

                //Toast.makeText(this, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()
                findViewById<TextView>(R.id.textoCalendario).text = y.toString() + "/" + m.toString() + "/" + d.toString()
                fecha = y.toString() + "/" + m.toString() + "/" + d.toString()
            }),year,month,day)

            tpd.show()
        }

    }

    private fun setOneTimeRequest() {
        val wm: WorkManager = WorkManager.getInstance(applicationContext)
        // val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val misDatos: Data = Data.Builder().putString(KEY_VALUE, mensaje).build()

        val uploadRequest=  PeriodicWorkRequest
            .Builder(UploadWorker::class.java, tiempo.toLong(), TimeUnit.SECONDS)
            .build()

        wm.enqueue(uploadRequest)
        //wm.enqueueUniquePeriodicWork("Chambrita",ExistingPeriodicWorkPolicy.REPLACE, uploadRequest)
        wm.getWorkInfoById(uploadRequest.id)
        wm.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, androidx.lifecycle.Observer {
            findViewById<TextView>(R.id.textview).text = "${it.state.name} \n ${it.id}"
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

    public fun lanzaNotificacion(titulo: String, cuerpo: String){
        val intent = Intent(this, NotificationsListener::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(this, 0, intent, 0)


        val constructor = NotificationCompat.Builder(this, "CHAFA")
            .setSmallIcon(R.drawable.small_icon_foreground)
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