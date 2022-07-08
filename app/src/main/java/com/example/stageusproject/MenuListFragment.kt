package com.example.stageusproject

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.ceil

class MenuListFragment : Fragment() {
    lateinit var menuData : List<MenuList>
    lateinit var retrofit : Retrofit
    lateinit var retrofitHttp : RetrofitService
    val textViewList = listOf(R.id.text1, R.id.text2, R.id.text3)
    val imageViewList = listOf(R.id.image1, R.id.image2, R.id.image3)
    val linearViewList = listOf(R.id.linear1, R.id.linear2, R.id.linear3)
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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.menu_list_fragment, container, false)
        val intent = Intent(activity, CartService::class.java)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        initRetrofit()
        initData(view)
        return view
    }

    fun initRetrofit(){
        retrofit = RetrofitClient.initRetrofit()!!
        retrofitHttp = retrofit.create(RetrofitService::class.java)
    }

    fun initData(view: View){
        val category = arguments?.getString("category")
        Log.d("category", category!!)
        retrofitHttp.getMenu(category!!, "kr")
            .enqueue(object : Callback<MenuData>{
                override fun onFailure(call: Call<MenuData>, t: Throwable) {
                    Log.d("result", "Request : ${t}")
                }

                override fun onResponse(call: Call<MenuData>, response: Response<MenuData>) {
                    if(response.body()!!.success){
                        menuData = response.body()!!.data
                        initEvent(view)
                    }
                    else{
                        Log.d("result", response.body()!!.message)
                    }
                }
            })

    }

    fun setView(content : View, contentNumber : Int, menuNumber : Int){ //View들의 동적할당 및 클릭이벤트 생성
        val linear = content.findViewById<LinearLayout>(linearViewList[contentNumber])
        content.findViewById<TextView>(textViewList[contentNumber]).text =
            "${menuData[menuNumber].menu_name}\n${menuData[menuNumber].menu_price}"
        Glide.with(content)
            .load("http://3.39.66.6:3000${menuData[menuNumber].menu_image}")
            .into(content.findViewById(imageViewList[contentNumber]))
        linear.setOnClickListener {
            val tempCart = InCartData(
                menuData[menuNumber].menu_name, menuData[menuNumber].menu_price,
                "http://3.39.66.6:3000${menuData[menuNumber].menu_image}"
            )
            val builder = AlertDialog.Builder(context)
            builder.setTitle(menuData[menuNumber].menu_name).setMessage("추가하시겠습니까?")
                .setPositiveButton("확인",
                DialogInterface.OnClickListener{dialog, id -> myService?.setCart(tempCart)})
                .setNegativeButton("취소",null)
            builder.show()
        }
    }

    fun initEvent(myView : View){
        var menuNumber = 0
        val rowNumber = ceil((menuData.size.toDouble()) / 3).toInt()
        val table = myView.findViewById<TableLayout>(R.id.coffeeTable)

        for(index in 0 until rowNumber){
            val content = layoutInflater.inflate(R.layout.menu_list_view, table, false)
            if(index+1 == rowNumber){
                if(menuData.size % 3 == 0){
                    for(contentNumber in 0 until 3){
                        setView(content, contentNumber, menuNumber)
                        menuNumber++
                    }
                }
                else{
                    for(contentNumber in 0 until (menuData.size % 3)){
                        setView(content, contentNumber, menuNumber)
                        menuNumber++
                    }
                }
            }
            else{
                for(contentNumber in 0 until 3){
                    setView(content, contentNumber, menuNumber)
                    menuNumber++
                }
            }
        table.addView(content)
        }
    }

}