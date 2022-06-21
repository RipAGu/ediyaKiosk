package com.example.stageusproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PaymentActivity : AppCompatActivity() {
    var myService: CartService? = null
    private var isBound = false
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            //coroutine사용 -> connection이 완료될 때 까지 mainThread 정지
            runBlocking {
                launch {
                    val binder = p1 as CartService.MyBinder
                    myService = binder.getService()
                    isBound = true
                    Log.d("connection", "success")
                }
            }
            initData()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_layout)
        val intent = Intent(this, CartService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    fun Context.resIdByName(resIdName: String?, resType: String): Int{ //R.id 경로를 int형으로 바꿔주는 함수
        resIdName?.let {
            return resources.getIdentifier(it, resType, packageName)
        }
        throw Resources.NotFoundException()
    }

    fun initData(){
        val name = myService?.getName()
        val price = myService?.getPrice()
        val imageUrl = myService?.getImage()
        var rowNumber = 0
        var totalPrice = 0

        if(name != null) {
            rowNumber = Math.ceil((name.size.toDouble() / 3)).toInt() //열의 개수
            for(index in 0 until name.size){
                totalPrice += price!![index].toInt()
            }
        }
        initEvent(name, imageUrl, rowNumber, totalPrice)
    }

    fun initEvent(name : ArrayList<String>?, imageUrl : ArrayList<String>? , rowNumber : Int, totalPrice : Int){
        val table = findViewById<TableLayout>(R.id.paymentTable)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val cardBtn = findViewById<Button>(R.id.cardBtn)
        val cashBtn = findViewById<Button>(R.id.cashBtn)
        val textTotalPrice = findViewById<TextView>(R.id.totalPrice)
        val text = arrayListOf<Int>()
        val image = arrayListOf<Int>()
        val linearArray = arrayListOf<Int>()
        var nameNumber = 0


        textTotalPrice.text = "총 금액 : ${totalPrice}원"

        for(index in 0 until 3){
            text.add(applicationContext.resIdByName("text"+(index+1), "id")!!)
            image.add(applicationContext.resIdByName("image"+(index+1), "id")!!)
            linearArray.add(applicationContext.resIdByName("linear"+(index+1), "id")!!)
        }
        //동적할당 시작
        for(index in 0 until rowNumber){
            val content = layoutInflater.inflate(R.layout.menu_list_view, table, false)
            if(index+1 == rowNumber){
                if(name!!.size % 3 == 0){
                    for(contentNumber in 0 until 3){
                        content.findViewById<TextView>(text[contentNumber]).text = name!![nameNumber]
                        content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl!![nameNumber].toInt())
                        nameNumber++
                    }
                }
                else{
                    for(contentNumber in 0 until name!!.size % 3){
                        content.findViewById<TextView>(text[contentNumber]).text = name!![nameNumber]
                        content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl!![nameNumber].toInt())
                        nameNumber++
                    }

                }

            }
            else {
                for(contentNumber in 0 until 3){
                    content.findViewById<TextView>(text[contentNumber]).text = name!![nameNumber]
                    content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl!![nameNumber].toInt())
                    nameNumber++
                }
            }
            table.addView(content)
        }
        //버튼 이벤트 등록
        backBtn.setOnClickListener{
            finish()
        }
        cardBtn.setOnClickListener{
            intent = Intent(applicationContext, OrderCompleteActivity::class.java)
            startActivity(intent)
        }
        cashBtn.setOnClickListener{
            intent = Intent(applicationContext, OrderCompleteActivity::class.java)
            startActivity(intent)
        }
    }
}

//코루틴
//init data init event 나누기기