package com.example.stageusproject

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class RemoveMenuActivity : AppCompatActivity() {
    val removePoint = arrayListOf<Int>()
    var myService: CartService? = null
    private var isBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            runBlocking {
                launch {
                    val binder = p1 as CartService.MyBinder
                    myService = binder.getService()
                    isBound = true
                    Log.d("connection", "success")
                }
            }
            initEvent()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remove_cart_layout)
        intent = Intent(this, CartService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        myService?.removeCart(removePoint)
    }


    fun initEvent(){
        val cartList = myService?.getCart()
        var totalPrice = 0
        val backBtn = findViewById<Button>(R.id.backBtn)
        val layout = findViewById<LinearLayout>(R.id.removeLayout)
        val textTotalPrice = findViewById<TextView>(R.id.totalPayment)
        if(cartList != null) {
            for (index in 0 until cartList.size) {
                val cart = layoutInflater.inflate(R.layout.remove_cart_view, layout, false)
                cart.findViewById<TextView>(R.id.nameText).text = cartList[index].name
                cart.findViewById<TextView>(R.id.priceText).text = cartList[index].price.toString()
                totalPrice += cartList[index].price.toInt()
                layout.addView(cart)
                cart.findViewById<LinearLayout>(R.id.removeLayout).setOnClickListener{
                    totalPrice -= cartList[index].price.toInt()
                    textTotalPrice.text = "총 금액 : ${totalPrice}원"
                    layout.removeView(cart)
                    removePoint.add(index)

                }
            }
        }
        textTotalPrice.text = "총 금액 : ${totalPrice}원"
        //기기의 뒤로가기버튼도 고려해서 이벤트를 등록해야됨!
        backBtn.setOnClickListener {
            myService?.removeCart(removePoint)
            intent.putExtra("point", removePoint)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}