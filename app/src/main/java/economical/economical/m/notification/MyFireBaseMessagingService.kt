package economical.economical.m.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import economical.economical.m.R
import java.util.*

class MyFireBaseMessagingService: FirebaseMessagingService() {
    lateinit var  title: String
    lateinit var  message:String
    var channelId = "CHANNEL_ID"
     override fun onMessageReceived(remoteMessage: RemoteMessage) {
         super.onMessageReceived(remoteMessage)
         title = remoteMessage.data["Title"]!!
         message = remoteMessage.data["Message"]!!
         var alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         var mediaPlayer = MediaPlayer.create(applicationContext, alarmSound)
         mediaPlayer.start()
         var builder = NotificationCompat.Builder(applicationContext, channelId)
                 .setSmallIcon(R.drawable.ic_launcher_foreground)
                 .setContentTitle(title)
                 .setContentText(message)
                 .setSound(alarmSound) //.setContentIntent(pendingIntent)
                 .setAutoCancel(true)
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .setDefaults(Notification.DEFAULT_ALL)
                 .setStyle(
                     NotificationCompat.BigTextStyle()
                         .bigText(message)
                 )
         val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channel = NotificationChannel(
                 channelId,
                 "Channel human readable title",
                 NotificationManager.IMPORTANCE_HIGH
             )
             channel.enableLights(true)
             channel.lightColor = R.color.button_color
             channel.shouldVibrate()
             channel.canShowBadge()
             channel.enableVibration(true)
             manager.createNotificationChannel(channel)
             builder.setChannelId(channelId)
         }

         manager.notify(Random().nextInt(), builder.build())

     }
}