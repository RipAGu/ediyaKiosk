package com.example.stageusproject

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PaymentActivity : AppCompatActivity() {
    val textViewList = listOf(R.id.text1, R.id.text2, R.id.text3)
    val imageViewList = listOf(R.id.image1, R.id.image2, R.id.image3)
    var myService: CartService? = null
    val orderListData = OrderListData("3", 3 ,3)
    lateinit var retrofit : Retrofit
    lateinit var retrofitHttp : RetrofitService

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
        initRetrofit()
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    fun initRetrofit(){
        retrofit = RetrofitClient.initRetrofit()!!
        retrofitHttp = retrofit.create(RetrofitService::class.java)
    }

    fun initData(){
        val cartList = myService?.getCart()
        var rowNumber = 0
        var totalPrice = 0

        if(cartList != null) {
            rowNumber = Math.ceil((cartList.size.toDouble() / 3)).toInt() //열의 개수
            for(index in 0 until cartList.size){
                totalPrice += cartList[index].price
            }
        }
        initEvent(cartList, rowNumber, totalPrice)
    }

    fun setView(cartList : ArrayList<InCartData>, content : View, contentNumber : Int, nameNumber : Int){
        content.findViewById<TextView>(textViewList[contentNumber]).text = cartList[nameNumber].name
        Glide.with(content).load(cartList[nameNumber].image).into(content.findViewById(imageViewList[contentNumber]))
    }

    fun setPaymentEvent(cartList : ArrayList<InCartData>?, totalPrice: Int){
        val id = myService?.id
        Log.d("size", cartList!!.size.toString())
        if(cartList.size == 0){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning").setMessage("장바구니가 비었습니다").setPositiveButton("확인",null)
            builder.show()
        }
        else{
            val orderList : Array<OrderListData?> = arrayOfNulls(cartList.size)
            for(index in 0 until cartList.size){
                val orderListData = OrderListData(cartList[index].name, 1, cartList[index].price)
                orderList[index] = orderListData
            }

            var requestData: HashMap<String, Any> = HashMap()
            requestData["id"] = id!!
            requestData["order_list"] = orderList
            requestData["total_price"] = totalPrice
            retrofitHttp.postOrder(requestData)
                .enqueue(object : Callback<PostOrder>{
                    override fun onFailure(call: Call<PostOrder>, t: Throwable) {
                        Log.d("result", "Request : ${t}")
                    }

                    override fun onResponse(
                        call: Call<PostOrder>,
                        response: Response<PostOrder>
                    ) {
                        if(response.body()!!.success){
                            Log.d("result", "success")
                            Log.d("menu?", orderList.toString())
                        }
                        else{
                            Log.d("result", response.body()!!.message)
                        }
                    }
                })
            Log.d("id", id!!)
            intent = Intent(applicationContext, OrderCompleteActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

    }

    fun initEvent(cartList : ArrayList<InCartData>?, rowNumber : Int, totalPrice : Int){
        val table = findViewById<TableLayout>(R.id.paymentTable)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val cardBtn = findViewById<Button>(R.id.cardBtn)
        val cashBtn = findViewById<Button>(R.id.cashBtn)
        val textTotalPrice = findViewById<TextView>(R.id.totalPrice)

        var nameNumber = 0
        textTotalPrice.text = "총 금액 : ${totalPrice}원"

        //동적할당 시작
        for(index in 0 until rowNumber){
            val content = layoutInflater.inflate(R.layout.menu_list_view, table, false)
            if(index+1 == rowNumber){
                if(cartList!!.size % 3 == 0){
                    for(contentNumber in 0 until 3){
                        setView(cartList, content, contentNumber, nameNumber)
                        nameNumber++
                    }
                }
                else{
                    for(contentNumber in 0 until cartList!!.size % 3){
                        setView(cartList, content, contentNumber, nameNumber)
                        nameNumber++
                    }
                }
            }
            else {
                for(contentNumber in 0 until 3){
                    setView(cartList!!, content, contentNumber, nameNumber)
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
            setPaymentEvent(cartList, totalPrice)

        }
        cashBtn.setOnClickListener{
            setPaymentEvent(cartList, totalPrice)
        }
    }
}

//코루틴
//init data init event 나누기기