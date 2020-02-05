package com.example.qr

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_qr_popup.*

class QRPopupActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_qr_popup)

        val Bank = intent.getStringExtra("bank")
        val Code = intent.getStringExtra("code")

        bank.text = Bank
        code.text = Code
        submit.setOnClickListener {
            val qr= "$Bank $Code"
            val intent = Intent(this, QRWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra("QR", qr)
            this.sendBroadcast(intent)
            Toast.makeText(this,"위젯에 등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        cancel.setOnClickListener {
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent):Boolean {
        if(event.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }

}

