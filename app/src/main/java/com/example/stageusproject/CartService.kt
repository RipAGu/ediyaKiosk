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
import java.io.Serializable

class CartService : Service() {
    val myBinder: IBinder = MyBinder()
    var totalPrice : Int = 0
    var cartList = ArrayList<InCartData>()


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
    fun setCart(cart : InCartData) {
        cartList.add(cart)
    }

    fun getCart() : ArrayList<InCartData>{
        return cartList
    }

    //장바구니 불러오기


    fun calTotalPrice(){
        for (index in 0 until cartList.size){
            totalPrice += cartList[index].price.toInt()
        }
    }

    // 장바구니 메뉴제거
    fun removeCart(receivePoint : ArrayList<Int>){
        var removeCount = 0
        var removePoint = receivePoint
        removePoint.sort()
        for(index in 0 until removePoint.size){
            cartList.removeAt(removePoint[index-removeCount])
            removeCount++
        }
    }

    //장바구니 비우기
    fun deleteCart(){
        cartList.clear()
    }

    //notification 구현
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123
    private fun generateForegroundNotification() {
        calTotalPrice()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //안드로이드 버전이 일정이상일 시 channel 필요
            val intentMainLanding = Intent(this, StartAcivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, 0) //다시돌아오겠다
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
                notificationChannel.enableLights(false) //알림표시등 표시여부
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET //잠금화면 표시여부
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" 총 금액은 : ${totalPrice}").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText("Touch to open") //
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent) //누를시 이동할 액티비티
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

data class InCartData(var name: String, var price: String, var image: String)