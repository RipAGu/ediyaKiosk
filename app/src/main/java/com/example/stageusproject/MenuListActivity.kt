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

//각각의 메뉴판에서 올 data들
interface CartData{
    fun sendData(coffeeName : String, coffeePrice : String, coffeeImage : String)
}

class MenuListActivity : AppCompatActivity(){
//    var nameInCart = arrayListOf<String>() //data취합
//    var priceInCart = arrayListOf<String>()
//    var imageInCart = arrayListOf<String>()
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
        val value = intent.getIntExtra("data", 0)
        initEvent(value)
    }



    fun initEvent(value : Int){

        Log.d("init", "event")

        val intent = Intent()
        val backBtn =findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener{
//            intent.putExtra("name", nameInCart) //activity종료시 보낼 data들 (고른 메뉴data들)
//            intent.putExtra("price", priceInCart)
//            intent.putExtra("image", imageInCart)
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
        when(value) {
            1 -> {
                val coldBrewFragment = MenuListColdbrewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, coldBrewFragment).commit()
            }
            2 -> {
                val espressoFragment = MenuListEspressoFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, espressoFragment).commit()
            }
            3 -> {
                val frappuccinoFragment = MenuListFrappuccinoFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, frappuccinoFragment).commit()
            }
            4 -> {
                val teaFragment = MenuListTeaFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, teaFragment).commit()
            }
        }

    }
}