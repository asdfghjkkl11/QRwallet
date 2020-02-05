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

        submit.setOnClickListener {
            val id = intent.getLongExtra("ID",-1)
            val bank = bank.text.toString()
            val code = code.text.toString()

            editFromRealm(id, bank, code)

            val intent = Intent(this,MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(this,"수정되었습니다.",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        cancel.setOnClickListener {
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
}
