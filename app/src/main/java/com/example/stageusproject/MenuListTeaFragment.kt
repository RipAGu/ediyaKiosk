package com.example.stageusproject

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class MenuListTeaFragment : Fragment() {
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
        initEvent(view)
        return view
    }

    fun Context.resIdByName(resIdName: String?, resType: String): Int{ //R.id 경로를 int형으로 바꿔주는 함수
        resIdName?.let {
            return resources.getIdentifier(it, resType, packageName)
        }
        throw Resources.NotFoundException()
    }

    fun initEvent(myView : View){
        val menuInfo = arrayOf(
            arrayOf("티", "아이스티", "tea_icetea", "6000"),
            arrayOf("티", "밀크티", "tea_milk", "4500"),
            arrayOf("티", "피치 얼그레이", "tea_peachalgray", "5500"),
            arrayOf("티", "석류 애플라임", "tea_applelime", "7500"),
            arrayOf("티", "석류 애플라임", "tea_applelime", "7500"),
            arrayOf("티", "석류 애플라임", "tea_applelime", "7500")
        )
        val text = arrayListOf<Int>()
        val image = arrayListOf<Int>()
        var nameNumber = 0
        val imageUrl = arrayListOf<Int>()
        val linearArray = arrayListOf<Int>()
        val rowNumber = Math.ceil((menuInfo.size.toDouble() / 3)).toInt() //열의 개수
        val table = myView.findViewById<TableLayout>(R.id.coffeeTable) //부모 tablelayout 선언

        for(index in 0 until menuInfo.size){
            imageUrl.add(context?.resIdByName(menuInfo[index][2], "mipmap")!!)
        }
        for(index in 0 until 3){
            text.add(context?.resIdByName("text"+(index+1), "id")!!)
            image.add(context?.resIdByName("image"+(index+1), "id")!!)
            linearArray.add(context?.resIdByName("linear"+(index+1), "id")!!)
        }
        //동적할당 시작
        for(index in 0 until rowNumber){
            val content = layoutInflater.inflate(R.layout.menu_list_view, table, false)
            if(index+1 == rowNumber){
                if(menuInfo.size % 3 == 0){
                    for(contentNumber in 0 until 3){
                        val imageData = imageUrl[nameNumber]
                        val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
                        content.findViewById<TextView>(text[contentNumber]).text = "${menuInfo[nameNumber][1]}\n${menuInfo[nameNumber][3]}"
                        content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl[nameNumber])
                        val nameToken = content.findViewById<TextView>(text[contentNumber]).text.toString().split('\n')
                        linear.setOnClickListener{
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{dialog, id -> myService?.setCart(nameToken[0],nameToken[1], imageData.toString()) })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener{dialog, id -> })
                            builder.show()
                        }
                        nameNumber++
                    }
                }
                else {
                    for (contentNumber in 0 until menuInfo.size % 3) {
                        val imageData = imageUrl[nameNumber]
                        val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
                        content.findViewById<TextView>(text[contentNumber]).text =
                            "${menuInfo[nameNumber][1]}\n${menuInfo[nameNumber][3]}"
                        content.findViewById<ImageView>(image[contentNumber])
                            .setImageResource(imageUrl[nameNumber])
                        val nameToken =
                            content.findViewById<TextView>(text[contentNumber]).text.toString()
                                .split('\n')
                        linear.setOnClickListener {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
                                .setPositiveButton("확인",
                                    DialogInterface.OnClickListener{dialog, id -> myService?.setCart(nameToken[0],nameToken[1], imageData.toString()) })
                                .setNegativeButton("취소",
                                    DialogInterface.OnClickListener{dialog, id -> })
                            builder.show()
                        }
                        nameNumber++
                    }
                }
            }
            else {
                for(contentNumber in 0 until 3){
                    val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
                    val imageData = imageUrl[nameNumber]
                    content.findViewById<TextView>(text[contentNumber]).text = "${menuInfo[nameNumber][1]}\n${menuInfo[nameNumber][3]}"
                    content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl[nameNumber])
                    val nameToken = content.findViewById<TextView>(text[contentNumber]).text.toString().split('\n')
                    linear.setOnClickListener{
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
                            .setPositiveButton("확인",
                                DialogInterface.OnClickListener{ dialog, id -> myService?.setCart(nameToken[0],nameToken[1], imageData.toString()) })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener{ dialog, id -> })
                        builder.show()
                    }
                    nameNumber++
                }
            }
            table.addView(content)
        }


    }
}