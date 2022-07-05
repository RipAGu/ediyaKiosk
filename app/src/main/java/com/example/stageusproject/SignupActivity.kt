package com.example.stageusproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignupActivity : AppCompatActivity() {
    lateinit var retrofit: Retrofit
    lateinit var retrofitHttp: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)
        initRetrofit()
        initEvnet()
    }

    fun initRetrofit(){
        retrofit = RetrofitClient.initRetrofit()!!
        retrofitHttp = retrofit.create(RetrofitService::class.java)
    }

    fun Alert(message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notice").setMessage(message).setPositiveButton("확인", null)
        builder.show()
    }

    fun initEvnet(){
        var dupCheck: Boolean = false
        var idValue: String = ""
        var pwValue: String = ""
        var pwCheck: String = ""
        var nameValue: String = ""
        var contactValue: String = ""
        val dupBtn = findViewById<Button>(R.id.dupBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val signupBtn = findViewById<Button>(R.id.signupBtn)

        dupBtn.setOnClickListener {
            idValue = findViewById<EditText>(R.id.idText).text.toString()
            Log.d("log", idValue)
            if(idValue == ""){
               Alert("아이디를 입력해주세요")

            }
            else {
                retrofitHttp.getOverlap(idValue)
                    .enqueue(object : Callback<AccountOverlap> {
                        override fun onFailure(call: Call<AccountOverlap>, t: Throwable) {
                            Log.d("result", "Request Fail ${t}")
                        }

                        override fun onResponse(
                            call: Call<AccountOverlap>,
                            response: Response<AccountOverlap>
                        ) {
                            if (response.body()!!.success) {
                                dupCheck = true
                                findViewById<EditText>(R.id.idText).isEnabled = false
                                Alert("사용가능합니다!")
                            } else {
                                Alert("중복 된 아이디입니다.")
                            }
                        }
                    })
            }
        }

        signupBtn.setOnClickListener {
            pwValue = findViewById<EditText>(R.id.pwText).text.toString()
            pwCheck = findViewById<EditText>(R.id.pwDupText).text.toString()
            nameValue= findViewById<EditText>(R.id.nameText).text.toString()
            contactValue = findViewById<EditText>(R.id.contactText).text.toString()
            if(dupCheck == false) {
                Alert("중복확인을 해주세요!")
            }
            else if(pwValue == ""){
                Alert("빈칸없이 입력헤주세요!")
            }
            else if(pwCheck == ""){
                Alert("빈칸없이 입력헤주세요!")
            }
            else if(nameValue == ""){
                Alert("빈칸없이 입력헤주세요!")
            }
            else if(contactValue == ""){
                Alert("빈칸없이 입력헤주세요!")
            }
            else if(pwValue != pwCheck){
                Alert("비밀번호가 동일하지 않습니다!")
            }
            else{
                var requsetData: HashMap<String, String> = HashMap()
                requsetData["id"] = idValue
                requsetData["pw"] = pwValue
                requsetData["name"] = nameValue
                requsetData["contact"] = contactValue
                retrofitHttp.postAccount(requsetData)
                    .enqueue(object: Callback<AccountData>{
                        override fun onFailure(call: Call<AccountData>, t: Throwable) {
                            Log.d("result", "request Fail : ${t}")
                        }

                        override fun onResponse(
                            call: Call<AccountData>,
                            response: Response<AccountData>
                        ) {
                            if(response.body()!!.success){
                                val builder = AlertDialog.Builder(this@SignupActivity)
                                builder.setTitle("Notice").setMessage("회원가입 성공").setPositiveButton("확인",
                                DialogInterface.OnClickListener{dialog, id -> finish()})
                                builder.show()
                            }
                            else{
                                Alert(response.body()!!.message)
                            }
                        }
                    })
            }
        }



        backBtn.setOnClickListener {
            finish()
        }
    }

}