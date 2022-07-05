package com.example.stageusproject

import android.app.AlertDialog
import android.app.Service
import android.content.*
import android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat

class StartActivity : AppCompatActivity() {
    var myService: CartService? = null
    private var isBound = false

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as CartService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }
    //홈버튼으로 앱을 내렸을 때의 이벤트
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        intent = Intent(this, CartService::class.java)

        ContextCompat.startForegroundService(this, intent)

    }

    //홈버튼에서 복귀했을 때 notification종료 후 서비스 재시작
    override fun onResume() {
        super.onResume()
        intent = Intent(this, CartService::class.java)
        stopService(intent)

        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val service = Intent(this, CartService::class.java)
        applicationContext.bindService(service, connection, Context.BIND_AUTO_CREATE)
        val startBtn = findViewById<Button>(R.id.startBtn)
        val delCartBtn = findViewById<Button>(R.id.delCartBtn)
        startBtn.setOnClickListener{
            val intent = Intent(applicationContext, OptionActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
            startActivity(intent)
        }
        delCartBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("장바구니 비우기").setMessage("장바구니를 비우시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id -> myService?.deleteCart()})
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener{ dialog, id -> })
            builder.show()
        }
    }


}