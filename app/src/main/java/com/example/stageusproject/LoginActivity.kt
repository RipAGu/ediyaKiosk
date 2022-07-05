package com.example.stageusproject

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {

    lateinit var retrofit : Retrofit
    lateinit var retrofitHttp : RetrofitService
    var myService : CartService? = null
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        val intent = Intent(this, CartService::class.java)
        applicationContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        initRetrofit()
        initEvent()
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

    fun initEvent(){
        var idValue: String
        var pwValue : String
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener{
            idValue = findViewById<EditText>(R.id.idText).text.toString()
            pwValue = findViewById<EditText>(R.id.pwText).text.toString()

            retrofitHttp.getAccount(idValue, pwValue)
                .enqueue(object : Callback<AccountLoginData> {
                    override fun onFailure(call: Call<AccountLoginData>, t: Throwable) {
                        Log.d("result", "ReQuest Fail : ${t}")
                    }

                    override fun onResponse(
                        call: Call<AccountLoginData>,
                        response: Response<AccountLoginData>) {
                        Log.d("result", "connection Succcess")
                        if(response.body()!!.success){
                            findViewById<EditText>(R.id.idText).setText(null)
                            findViewById<EditText>(R.id.pwText).setText(null)
                            val intent = Intent(applicationContext, StartActivity::class.java)
                            myService?.id = idValue
                            startActivity(intent)
                        }
                        else{
                            Log.d("result", response.body()!!.message)
                            Alert("아이디 또는 비밀번호가 일치하지 않습니다!")
                        }
                    }
                })

        }
        val signupBtn = findViewById<Button>(R.id.signupBtn)
        signupBtn.setOnClickListener{
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }

    }


}