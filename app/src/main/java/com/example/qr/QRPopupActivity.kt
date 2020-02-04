package com.example.qr

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window

import kotlinx.android.synthetic.main.activity_qr_popup.*

class QRPopupActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_qr_popup)

        val Bank = intent.getStringExtra("bank")
        val Code = intent.getStringExtra("code")
        //UI 객체생성

        submit.setOnClickListener {
            val qr= "$Bank $Code"
            val intent = Intent(this, QRWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra("QR", qr)
            this.sendBroadcast(intent)
            finish()
        }

        cancel.setOnClickListener {
            finish()
        }

        bank.text = Bank
        code.text = Code
        //데이터 가져오기
    }

    fun mOnClose(v: View){
        //데이터 전달하기
        val intent = Intent()
        intent.putExtra("result", "Close Popup")
        setResult(RESULT_OK, intent)

        //액티비티(팝업) 닫기
        finish()
    }

    override fun onTouchEvent(event: MotionEvent):Boolean {
        //바깥레이어 클릭시 안닫히게
        if(event.action == MotionEvent.ACTION_OUTSIDE){
            return false
        }
        return true
    }

    override fun onBackPressed() {
        //안드로이드 백버튼 막기
        return
    }
}

