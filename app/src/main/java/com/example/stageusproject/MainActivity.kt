package com.example.stageusproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.ArrayList


interface DataFromFragment{ //coffeefreagment에서 올 Data
    fun sendData(requestType: String, requestData: ArrayList<String>?)
}
interface DataFromRemover{ //menuFragment에서 올 Data
    fun sendRemove(requestData: ArrayList<Int>)
}

interface ChangeFragment{
    fun change(requestData: Int)
}

class OptionActivity : AppCompatActivity(), DataFromFragment, DataFromRemover, ChangeFragment{
    var nameInCart = arrayListOf<String>()
    var priceInCart = arrayListOf<String>()
    var imageInCart = arrayListOf<String>()
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

    override fun sendData(requestType: String, requestData: ArrayList<String>?) {
        if(requestType == "name") nameInCart.addAll(requestData!!) //받아온 주문목록 취합
        else if(requestType == "price") priceInCart.addAll(requestData!!)
        else if(requestType == "image") imageInCart.addAll(requestData!!)
    }
    //받아온 제거 포인트를 이용해 기존장바구니 삭제
    override fun sendRemove(requestData: ArrayList<Int>) {
        var removeCount = 0
        var removePoint = requestData
        removePoint.sort()
        for(index in 0 until requestData.size){
            Log.d("removeCount", removeCount.toString())
            Log.d("index", index.toString())
            Log.d("nameInCart", nameInCart.toString())
            nameInCart.removeAt(removePoint[index-removeCount])
            priceInCart.removeAt(removePoint[index-removeCount])
            imageInCart.removeAt(removePoint[index-removeCount])
            removeCount++
        }
    }
    //하위 fragment로부터 데이터를 받아 fragment 교체
    override fun change(requestData: Int) {
        when(requestData){
            1 -> {
                val coffeeFragment = MainCoffeeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, coffeeFragment).commit()
            }
            2 -> {
                val menuFragment = MainMenuFragment()
                val bundle = Bundle()
                bundle.putStringArrayList("name", nameInCart)
                bundle.putStringArrayList("price", priceInCart)
                bundle.putStringArrayList("image", imageInCart)
                Log.d("메뉴프래그먼트로갈때 데이터", nameInCart.toString())
                menuFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, menuFragment).commit()
            }
            3 -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.option_list_layout)
        Glide.with(this).load(R.mipmap.ediyalogo2).into(findViewById(R.id.optionTopLogo))

        val menuFragment = MainMenuFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, menuFragment).commit()
        var service = Intent(this, CartService::class.java)
        bindService(service, connection, Context.BIND_AUTO_CREATE)
    }



}