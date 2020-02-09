package com.example.qr

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_qr_popup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QRCodeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_qr_popup)

        val key = ""
        val Bank = intent.getStringExtra("bank")
        val Code = intent.getStringExtra("code")
        val req = RequestModel(key,Bank,Code)

        bankXML.text = Bank
        codeXML.text = Code
        textXML.text = "작업을 선택해 주세요"
        submitXML.text = "클립보드에 복사"
        submitXML.setOnClickListener {
            saveClip("$Bank $Code")
            Toast.makeText(this,"클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        cancelXML.text = "Toss앱 실행"
        cancelXML.setOnClickListener {
            callTossAPI(req)
            Toast.makeText(this,"토스앱이 실행됩니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            return false
        }
        return true
    }

    private fun saveClip(str : String){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = ClipData.newPlainText("clip",str)
    }

    private fun callTossAPI(req:RequestModel){
        val call = getService().postJson(req)

        call.enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {}

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val res= response.body()
                if(res!!.resultType=="SUCCESS"){
                    intent = Intent(Intent.ACTION_VIEW)
                    val uri = Uri.parse(res.success.link)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        })
    }

}