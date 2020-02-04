package com.example.qr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(){
    private var accountList = ArrayList<account>()
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        init()
        setUI()
        camera.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(result != null){
            if(result.contents != null){
                val str = result.contents.split(" ")
                val key = "9cadbe3e1e3c4676b1181f5f015f0dee"
                val req = RequestModel(key,str[0],str[1])
                //saveClip(result.contents)
                callTossAPI(req)
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun init(){
        Realm.init(this)
        realm = Realm.getDefaultInstance()
        val result = realm.where<RealmModel>().findAll()!!

        for(i in 0 until result.size){
            val bank = result[i]!!.bank
            val code = result[i]!!.code
            accountList.add(account(result[i]!!.ID,bank,code,makeQR("$bank $code")))
        }
        accountList.add(account((-2).toLong(),"","",makeQR("")))
    }
    private fun setUI(){
        val accountAdapter = Adpater(this, accountList, viewPager)
        viewPager.adapter = accountAdapter

        edit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("ID",accountList[viewPager.currentItem].ID)
            startActivity(intent)
        }

        delete.setOnClickListener {
            val intent = Intent(this, DeleteActivity::class.java)
            intent.putExtra("ID",accountList[viewPager.currentItem].ID)
            startActivity(intent)
        }
    }

    private fun makeQR(text:String): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()

        try {
            val barcodeManager = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,800,800)
            val barcodeEncoder = BarcodeEncoder()
            return barcodeEncoder.createBitmap(barcodeManager)
        }catch (e:Exception){}

        return null
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

