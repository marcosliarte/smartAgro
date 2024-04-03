package com.example.smartagro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.smartagro.R

class LembreteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val dataAdubacao = intent.getStringExtra("DATA_ADUBACAO")
            if (dataAdubacao != null) {
                // Aqui você pode adicionar a lógica para criar um lembrete
                criarLembrete(context, dataAdubacao)
            }
        }
    }

    private fun criarLembrete(context: Context, dataAdubacao: String) {
        val channelId = "lembrete_channel"
        val notificationId = 1001

        // Cria um intent para abrir a atividade quando a notificação for clicada
        val intent = Intent(context, R.drawable.ic_launcher_foreground::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Cria um canal de notificação, necessário para dispositivos com Android Oreo (API 26) e superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes"
            val descriptionText = "Canal para lembretes de adubação"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Registra o canal no sistema
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Cria a notificação
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Lembrete de adubação")
            .setContentText("Adubação em $dataAdubacao")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Mostra a notificação
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}
