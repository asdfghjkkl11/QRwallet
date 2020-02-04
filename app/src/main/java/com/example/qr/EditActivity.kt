package com.example.qr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {
    private val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        submit.setOnClickListener {
            val id = intent.getLongExtra("ID",-1)
            val bank = bank.text.toString()
            val code = code.text.toString()
            editFromRealm(id, bank, code)
            goMainActivity()
        }

        cancel.setOnClickListener {
            goMainActivity()
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

    private fun goMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
