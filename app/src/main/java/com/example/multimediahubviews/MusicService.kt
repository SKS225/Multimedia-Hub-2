package com.example.multimediahubviews

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }


    fun showNotification(playPauseBtn: Int) {

        val prevIntent = Intent(
            baseContext,
            NotificationReceiver::class.java
        ).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val exitIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(
            baseContext,
            0,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val imgArt = getImgArt(AudioPlayer.musicListPA[AudioPlayer.songPosition].path)
        val image = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.music)
        }

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(AudioPlayer.musicListPA[AudioPlayer.songPosition].title)
            .setContentText(AudioPlayer.musicListPA[AudioPlayer.songPosition].title)
            .setSmallIcon(R.drawable.play)
            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.launcher_icon))
            .setLargeIcon(image)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.baseline_skip_previous_24, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.baseline_skip_next_24, "Next", nextPendingIntent)
            .addAction(R.drawable.baseline_exit_to_app_24, "Exit", exitPendingIntent)
            .build()

        /*val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }*/


        //startForegroundService(Intent(baseContext,MusicService::class.java))

        startForeground(2, notification)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createMediaPlayer() {
        try {
            if (AudioPlayer.musicService!!.mediaPlayer == null) AudioPlayer.musicService!!.mediaPlayer =
                MediaPlayer()
            AudioPlayer.musicService!!.mediaPlayer?.reset()
            AudioPlayer.musicService!!.mediaPlayer?.setDataSource(AudioPlayer.musicListPA[AudioPlayer.songPosition].path)
            AudioPlayer.musicService!!.mediaPlayer?.prepare()
            AudioPlayer.binding.playPauseBtnPA.setIconResource(R.drawable.baseline_pause_24)
            AudioPlayer.musicService!!.showNotification(R.drawable.baseline_pause_24)

        } catch (e: Exception) {
            return
        }
    }
}