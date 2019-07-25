package com.feeling.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.feeling.moneyinputview.MoneyKeyboardView

class MainActivity : AppCompatActivity() {

    var edAmount: MoneyEditTextView? = null
    var keyboardView: MoneyKeyboardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edAmount = findViewById(R.id.amount)
        keyboardView = findViewById(R.id.keyboard)

        keyboardView?.setOnclickListener(object : MoneyKeyboardView.OnClickListener {
            override fun onClickKey(keyValue: Any) {
                if(keyValue is String){
                    edAmount?.addText(keyValue)
                }
            }

            override fun onConfirm() {
                Toast.makeText(this@MainActivity,"amount: ${edAmount?.getAmount()}", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete() {
                edAmount?.deleteText()
            }
        })
    }
}
