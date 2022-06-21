package com.example.stageusproject

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class CartService : Service() {
    val myBinder: IBinder = MyBinder()
    var nameInCart = arrayListOf<String>()
    var priceInCart = arrayListOf<String>()
    var imageInCart = arrayListOf<String>()


    override fun onBind(p0: Intent?): IBinder? {
        Log.d("서비스", "실행")
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        generateForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    inner class MyBinder : Binder(){
        fun getService(): CartService = this@CartService
    }

    //장바구니 추가
    fun setCart(name:String, price : String, image : String) {
        nameInCart.add(name)
        priceInCart.add(price)
        imageInCart.add(image)
    }

    //장바구니 불러오기
    fun getName() : ArrayList<String>{
        return nameInCart
    }
    fun getPrice() : ArrayList<String>{
        return priceInCart
    }
    fun getImage() : ArrayList<String>{
        return imageInCart
    }

    // 장바구니 메뉴제거
    fun removeCart(receivePoint : ArrayList<Int>){
        var removeCount = 0
        var removePoint = receivePoint
        removePoint.sort()
        for(index in 0 until removePoint.size){
            nameInCart.removeAt(removePoint[index-removeCount])
            priceInCart.removeAt(removePoint[index-removeCount])
            imageInCart.removeAt(removePoint[index-removeCount])
            removeCount++
        }
    }

    //장바구니 비우기
    fun deleteCart(){
        nameInCart.clear()
        priceInCart.clear()
        imageInCart.clear()
    }

    //notification 구현
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123
    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, StartAcivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chats_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel("service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN)
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append("노티ㅎㅎ^^").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText("Touch to open") //                    , swipe down for more options.
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }

    }



}