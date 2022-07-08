package com.example.stageusproject

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuListActivity : AppCompatActivity(){
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

//    override fun sendData(coffeeName: String, coffeePrice: String, coffeeImage: String){
//        nameInCart.add(coffeeName)
//        priceInCart.add(coffeePrice)
//        imageInCart.add(coffeeImage)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_list_layout)
        val service = Intent(this, CartService::class.java)
        applicationContext.bindService(service, connection, Context.BIND_AUTO_CREATE)
        val value = intent.getStringExtra("data")
        initEvent(value!!)
    }



    fun initEvent(value : String){
        val intent = Intent()
        val backBtn =findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener{
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        var menuFragment = MenuListFragment()
        var bundle = Bundle()
        bundle.putString("category", value)
        menuFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, menuFragment).commit()


    }
}