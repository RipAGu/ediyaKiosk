package com.example.stageusproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class OrderCompleteActivity : AppCompatActivity() {
    lateinit var retrofit : Retrofit
    lateinit var retrofitHttp : RetrofitService
    var myService: CartService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as CartService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_complete_layout)
        val intent = Intent(this, CartService::class.java)
        applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        initRetrofit()
        initEvent()
    }

    fun initRetrofit(){
        retrofit = RetrofitClient.initRetrofit()!!
        retrofitHttp = retrofit.create(RetrofitService::class.java)
    }
    fun initEvent(){
        val id = intent.getStringExtra("id")

        val orderListBtn = findViewById<Button>(R.id.orderListBtn)
        val completeBtn = findViewById<Button>(R.id.completeBtn)
        completeBtn.setOnClickListener{
            myService?.deleteCart()
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //시작액티비티하나만 살리고 나머지는 지움
            startActivity(i)
        }

        orderListBtn.setOnClickListener{
            retrofitHttp.getOrder(id!!)
                .enqueue(object : Callback<GetOrder>{
                    override fun onFailure(call: Call<GetOrder>, t: Throwable) {
                        Log.d("result" , "request : ${t}")
                    }

                    override fun onResponse(call: Call<GetOrder>, response: Response<GetOrder>) {
                        Log.d("OrderList", response.body()!!.data.toString())
                    }
                })

        }
    }
}