package com.example.qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : Activity() {
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_edit)
        val id = intent.getLongExtra("ID",-1)
        var bank = intent.getStringExtra("bank")
        var code = intent.getStringExtra("code")
        if(id!=(-1).toLong()){
            bankXML.text = bank
            codeXML.setText(code)
        }
        bankXML.setOnClickListener {
            val intent = Intent(this,BankActivity::class.java)
            startActivityForResult(intent, 0)
        }
        submitXML.setOnClickListener {
            bank = bankXML.text.toString()
            code = codeXML.text.toString()
            if(bank=="검색하기") {
                Toast.makeText(this, "은행을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                editFromRealm(id, bank, code)
                val intent = Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }
        cancelXML.setOnClickListener {
            finish()
        }
    }

    private fun editFromRealm(ID : Long, Bank : String, Code : String){
        var id =ID
        val max = realm.where<RealmModel>().max("ID")

        realm.executeTransaction { realm ->
            val account: RealmModel

            if(id!=(-1).toLong()) {
                account = realm.where<RealmModel>().equalTo("ID", id).findFirst()!!
            }else{
                id = if(max==null)
                    1
                else
                    max as Long + 1
                account = realm.createObject(id)
            }
            account.bank = Bank
            account.code = Code
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            return false
        }
        return true
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1){
            val str = data!!.getStringExtra("bank")
            bankXML.text = str
        }
    }
}
