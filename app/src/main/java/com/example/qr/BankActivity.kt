package com.example.qr

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_bank.*

class BankActivity : Activity(){
    private val bank = "NH농협,KB국민,신한," +
            "우리,KEB하나,IBK기업," +
            "SC제일,씨티,KDB산업," +
            "SBI저축은행,새마을,대구," +
            "광주,우체국,신협," +
            "전북,경남,부산," +
            "수협,제주,저축은행," +
            "산림조합,케이뱅크,카카오뱅크," +
            "HSBC,중국공상,JP모간," +
            "도이치,BNP파리바,BOA," +
            "중국건설"
    private val stock = "키움,KB증권,미래에셋대우," +
            "삼성,NH투자,유안타," +
            "대신,한국투자,신한금융투자," +
            "유진투자,한화투자,DB금융투자," +
            "하나금융,하이투자,현대차투자," +
            "신영,이베스트,교보," +
            "메리츠종금,KTB투자,SK," +
            "부국,케이프투자,펀드온라인코리아"
    private var res = "검색하기"
    private var banks = bank.split(",")
    private var stocks = stock.split(",")
    private var btnList = ArrayList<Button>()
    private lateinit var linearLayout : LinearLayout
    private lateinit var layout : LinearLayout
    private lateinit var param : LinearLayout.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        param.gravity = Gravity.CENTER
        param.weight = 1.0f
        param.setMargins(1)
        addBtn(banks, "은행")
        addBtn(stocks, "증권사")
        scrollViewXML.addView(layout)
        submitXML.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("bank",res)
            setResult(1,intent)
            finish()
        }
        cancelXML.setOnClickListener {
            finish()
        }
    }
    private fun addBtn(list: List<String>,str: String){
        val text = TextView(this)
        val bList = ArrayList<Button>()
        val view = View(this)

        view.setBackgroundColor(Color.parseColor("#3F51B5"))
        view.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,10)
        text.setTextColor(Color.parseColor("#3F51B5"))
        text.setBackgroundColor(Color.parseColor("#FFFFFF"))
        text.layoutParams = param
        text.text = str
        text.setPadding(100,16,16,16)
        text.textSize = 20.0f
        layout.addView(text)
        layout.addView(view)
        for(i in list.indices) {
            val btn = Button(this)
            btn.layoutParams = param
            btn.textSize = 15.0f
            btn.setPadding(32,32,32,32)
            btn.text = list[i]
            btn.setBackgroundColor(Color.parseColor("#FFFFFF"))
            btn.setOnClickListener {

                for(b in btnList){
                    b.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                if(res != list[i]) {
                    btn.setBackgroundColor(Color.parseColor("#3F51B5"))
                    res = list[i]
                }else{
                    res = "검색하기"
                }
            }
            btnList.add(btn)
            bList.add(btn)
        }

        for(i in bList.indices){
            if(i % 3 == 0){
                linearLayout = LinearLayout(this)
                linearLayout.layoutParams = param
            }
            linearLayout.addView(bList[i])
            val mod = i % 3
            if(mod == 2 || i == bList.size - 1){
                for(i in 0..1-mod){
                    val textView = TextView(this)
                    textView.layoutParams = param
                    textView.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                    textView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    linearLayout.addView(textView)
                }
                layout.addView(linearLayout)
            }
        }
    }
}