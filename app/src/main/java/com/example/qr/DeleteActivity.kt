package com.example.qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_delete.*

class DeleteActivity : Activity() {
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_delete)

        submit.setOnClickListener {
            val id= intent.getLongExtra("ID",-1)
            deleteFromRealm(id)
            val intent = Intent(this,MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(this,"삭제되었습니다.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        cancel.setOnClickListener {
            finish()
        }
    }

    private fun deleteFromRealm(id:Long){
        realm.beginTransaction()
        val result=realm.where<RealmModel>().equalTo("ID",id).findAll()
        result.deleteAllFromRealm()
        realm.commitTransaction()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_OUTSIDE) {
            return false
        }
        return true
    }

}