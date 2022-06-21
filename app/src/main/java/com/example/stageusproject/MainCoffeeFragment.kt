package com.example.stageusproject

import android.app.Activity.RESULT_OK
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class MainCoffeeFragment : Fragment() {
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
        initEvent(view)
        return view
    }

    fun initEvent(myView: View){
        val changeInterface = context as ChangeFragment
        val backBtn = myView.findViewById<Button>(R.id.backBtn)
        val coldBrewBtn = myView.findViewById<Button>(R.id.optionColdbrewBtn)
        val espressoBtn = myView.findViewById<Button>(R.id.optionEspressoBtn)
        val frappuccinoBtn = myView.findViewById<Button>(R.id.optionFrappuccinoBtn)
        val teaBtn = myView.findViewById<Button>(R.id.optionTeaBtn)

        //interface로 Acitity에 data 전송 -> fragment교체
        backBtn.setOnClickListener{
            changeInterface.change(2)
        }

        coldBrewBtn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", 1)
            startActivity(intent)
        }
        espressoBtn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", 2)
            startActivity(intent)
        }
        frappuccinoBtn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", 3)
            startActivity(intent)

        }
        teaBtn.setOnClickListener{
            val intent = Intent(context, MenuListActivity::class.java)
            intent.putExtra("data", 4)
            startActivity(intent)

        }
    }
}