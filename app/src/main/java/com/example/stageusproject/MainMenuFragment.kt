package com.example.stageusproject

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class MainMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.option_list_menu_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initEvent(myView: View){
        val changeInterface = context as ChangeFragment
        val addCartBtn =myView.findViewById<Button>(R.id.addCartBtn)
        val backBtn = myView.findViewById<Button>(R.id.backBtn)
        val paymentBtn = myView.findViewById<Button>(R.id.paymentBtn)
        val removeBtn = myView.findViewById<Button>(R.id.removeMenuBtn)

        addCartBtn.setOnClickListener{
            changeInterface.change(1)
        }

        backBtn.setOnClickListener{
            changeInterface.change(3)
        }

        paymentBtn.setOnClickListener{
            val intent = Intent(context, PaymentActivity::class.java)
            startActivity(intent)
        }

        removeBtn.setOnClickListener{
            val intent = Intent(context, RemoveMenuActivity::class.java)
            startActivity(intent)
        }

    }
}