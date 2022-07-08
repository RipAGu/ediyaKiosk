package com.example.stageusproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainCoffeeFragment : Fragment() {
    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService
    //startForResult -> activity종료시 자동으로 data를 받아옴
//    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        if(it.resultCode == RESULT_OK){
//            val getName = it.data?.getStringArrayListExtra("name")
//            val getPrice = it.data?.getStringArrayListExtra("price")
//            val getImage = it.data?.getStringArrayListExtra("image")
//            val datainterface =context as DataFromFragment //acrivity로 보낼 data
//            datainterface.sendData("name", getName)
//            datainterface.sendData("price", getPrice)
//            datainterface.sendData("image", getImage)
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.option_list_coffee_fragment, container, false)
        initRetrofit()
        initData(view)
        return view
    }

    fun initRetrofit(){
        retrofit = RetrofitClient.initRetrofit()!!
        retrofitHttp = retrofit.create(RetrofitService::class.java)
    }

    fun initData(view: View){
        var category: List<CategoryName>
        retrofitHttp.getCategory("en")
            .enqueue(object: Callback<CategoryData>{
                override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                    Log.d("result", "Request : ${t}")
                }

                override fun onResponse(
                    call: Call<CategoryData>,
                    response: Response<CategoryData>
                ) {
                    Log.d("result", "connection success")

                    if(response.body()!!.success){
                        Log.d("result", "category success")

                        category = response.body()!!.data
                        initEvent(view, category)

                    }
                    else{
                        Log.d("result", response.body()!!.message)
                    }
                }
            })
    }

    fun initEvent(myView: View, category: List<CategoryName>){
        val changeInterface = context as ChangeFragment
        val backBtn = myView.findViewById<Button>(R.id.backBtn)
        val option1Btn = myView.findViewById<Button>(R.id.option1)
        val option2Btn = myView.findViewById<Button>(R.id.option2)
        option1Btn.text = category[0].category_name
        option2Btn.text = category[1].category_name

        backBtn.setOnClickListener{
            changeInterface.change(2)
        }

        option1Btn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", "커피")
            startActivity(intent)
        }
        option2Btn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", "음료")
            startActivity(intent)
        }

    }
}