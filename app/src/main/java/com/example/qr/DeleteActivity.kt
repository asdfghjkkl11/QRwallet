package com.example.qr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_delete.*

class DeleteActivity : AppCompatActivity() {
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        submit.setOnClickListener {
            val id= intent.getLongExtra("ID",-1)
            deleteFromRealm(id)
            goMainActivity()
        }

        cancel.setOnClickListener {
            goMainActivity()
        }
    }

    private fun deleteFromRealm(id:Long){
        realm.beginTransaction()
        val result=realm.where<RealmModel>().equalTo("ID",id).findAll()
        result.deleteAllFromRealm()
        realm.commitTransaction()
    }

    private fun goMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}