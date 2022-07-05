//package com.example.stageusproject
//
//import android.app.AlertDialog
//import android.content.*
//import android.content.res.Resources
//import android.os.Bundle
//import android.os.IBinder
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.fragment.app.Fragment
//import com.bumptech.glide.Glide
//import com.google.gson.Gson
//
//class MenuListEspressoFragment : Fragment() {
//    var myService: CartService? = null
//    private var isBound = false
//
//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
//            val binder = p1 as CartService.MyBinder
//            myService = binder.getService()
//            isBound = true
//        }
//
//        override fun onServiceDisconnected(p0: ComponentName?) {
//            isBound = false
//        }
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.menu_list_fragment, container, false)
//        val intent = Intent(activity, CartService::class.java)
//        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
//        initEvent(view)
//        return view
//    }
//
//
//    fun Context.resIdByName(resIdName: String?, resType: String): Int{ //R.id 경로를 int형으로 바꿔주는 함수
//        resIdName?.let {
//            return resources.getIdentifier(it, resType, packageName)
//        }
//        throw Resources.NotFoundException()
//    }
//
//    fun initEvent(myView: View){
//        val menuInfo = arrayOf(
//            arrayOf("에스프레소", "에스프레소 콘 파나", "espresso_confana", "4000"),
//            arrayOf("에스프레소", "에스프레소 마키아또", "espresso_makiato", "4000"),
//            arrayOf("에스프레소", "아이스 카페 아메리카노", "espresso_americano", "4000"),
//            arrayOf("에스프레소", "카푸치노", "espresso_cafuchino", "4000")
//        )
//        val menuData = "{" +
//                "'menuList' : [" +
//                "{'name' : '에스프레소 콘 파나', 'price' : 5000, 'image' : espresso_confana}," +
//                "{'name' : '에스프레소 마키아또', 'price' : 4500, 'image' : espresso_makiato}," +
//                "{'name' : '아이스 카페 아메리카노', 'price' : 5000, 'image' : espresso_americano}," +
//                "{'name' : '카푸치노', 'price' : 6500, 'image' : espresso_cafuchino}," +
//                "]}"
//        data class MenuContents(
//            val name : String,
//            val price : Int,
//            val image : String
//        )
//
//        data class MenuList(
//            val menuList : ArrayList<MenuContents>
//        )
//
//
//        val gson = Gson()
//
//        val menuGson = gson.fromJson(menuData, MenuList::class.java)
//
//
//        // text, image, linear값을 동적으로 할당하기위해 residbyname함수에서 받아올 data들
//        val text = arrayListOf<Int>()
//        val image = arrayListOf<Int>()
//        val linearArray = arrayListOf<Int>()
//        val imageUrl = arrayListOf<Int>()
//
//
//        var nameNumber = 0 //출력될 menu의 번호
//        val rowNumber = Math.ceil((((menuGson.menuList.size.toDouble()) - 1) / 3)).toInt() //열의 개수
//        Log.d(rowNumber.toString(), "D")
//        val table = myView.findViewById<TableLayout>(R.id.coffeeTable) //부모 tablelayout 선언
//
//        for(index in 0 until menuGson.menuList.size-1){
//            imageUrl.add(context?.resIdByName(menuGson.menuList[index].image, "mipmap")!!)
//        }
//
//        for(index in 0 until 3){
//            text.add(context?.resIdByName("text"+(index+1), "id")!!)
//            image.add(context?.resIdByName("image"+(index+1), "id")!!)
//            linearArray.add(context?.resIdByName("linear"+(index+1), "id")!!)
//        }
//
//        for(index in 0 until rowNumber){ //동적생성 시작
//            val content = layoutInflater.inflate(R.layout.menu_list_view, table, false)
//            if(index+1 == rowNumber){
//                if((menuGson.menuList.size-1) % 3 == 0){
//                    for(contentNumber in 0 until 3){
//                        val imageData = imageUrl[nameNumber]
//                        val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
//                        content.findViewById<TextView>(text[contentNumber]).text =
//                            "${menuGson.menuList[nameNumber].name}\n${menuGson.menuList[nameNumber].price}"
//                        Glide.with(content).load(imageUrl[nameNumber]).into(content.findViewById(image[contentNumber]))
////                        content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl[nameNumber])
//                        val nameToken = content.findViewById<TextView>(text[contentNumber]).text.toString().split('\n')
//                        linear.setOnClickListener{
//                            val tempCart = InCartData(nameToken[0], nameToken[1], imageData.toString())
//
//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
//                                .setPositiveButton("확인",
//                                    DialogInterface.OnClickListener{dialog, id -> myService?.setCart(tempCart)})
//                                .setNegativeButton("취소",
//                                    DialogInterface.OnClickListener{dialog, id -> })
//                            builder.show()
////                            val dataInterface = context as CartData
////                            dataInterface.sendData(nameToken[0], nameToken[1], imageData.toString())
//                        }
//                        nameNumber++
//                    }
//                }
//                else {
//                    for (contentNumber in 0 until (menuGson.menuList.size-1) % 3) {
//                        val imageData = imageUrl[nameNumber]
//                        val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
//                        content.findViewById<TextView>(text[contentNumber]).text =
//                            "${menuGson.menuList[nameNumber].name}\n${menuGson.menuList[nameNumber].price}"
////                        content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl[nameNumber])
//                        Glide.with(content).load(imageUrl[nameNumber]).into(content.findViewById(image[contentNumber]))
//
//                        val nameToken =
//                            content.findViewById<TextView>(text[contentNumber]).text.toString()
//                                .split('\n')
//                        linear.setOnClickListener {
//                            val tempCart = InCartData(nameToken[0], nameToken[1], imageData.toString())
//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
//                                .setPositiveButton("확인",
//                                    DialogInterface.OnClickListener{dialog, id -> myService?.setCart(tempCart)})
//                                .setNegativeButton("취소",
//                                    DialogInterface.OnClickListener{dialog, id -> })
//                            builder.show()
////                            val dataInterface = context as CartData
////                            dataInterface.sendData(nameToken[0], nameToken[1], imageData.toString())
//                        }
//                        nameNumber++
//                    }
//                }
//            }
//
//            else {
//                for(contentNumber in 0 until 3){
//                    val linear = content.findViewById<LinearLayout>(linearArray[contentNumber])
//                    val imageData = imageUrl[nameNumber]
//                    content.findViewById<TextView>(text[contentNumber]).text =
//                        "${menuGson.menuList[nameNumber].name}\n${menuGson.menuList[nameNumber].price}"
////                    content.findViewById<ImageView>(image[contentNumber]).setImageResource(imageUrl[nameNumber])
//                    Log.d("ImageURL", imageUrl[nameNumber].toString())
//                    Glide.with(content).load(imageUrl[nameNumber]).into(content.findViewById(image[contentNumber]))
//
//                    val nameToken = content.findViewById<TextView>(text[contentNumber]).text.toString().split('\n')
//                    linear.setOnClickListener{
//                        val tempCart = InCartData(nameToken[0], nameToken[1], imageData.toString())
//                        val builder = AlertDialog.Builder(context)
//                        builder.setTitle(nameToken[0]).setMessage("추가하시겠습니까?")
//                            .setPositiveButton("확인",
//                                DialogInterface.OnClickListener{dialog, id -> myService?.setCart(tempCart)})
//                            .setNegativeButton("취소",
//                                DialogInterface.OnClickListener{dialog, id -> })
//                        builder.show()
////                        val dataInterface = context as CartData
////                        dataInterface.sendData(nameToken[0], nameToken[1], imageData.toString())
//                    }
//                    nameNumber++
//                }
//            }
//            table.addView(content)
//        }
//
//
//    }
//}
//
