package com.example.qr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(){
    private var accountList = ArrayList<account>()
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setUI()

        cameraXML.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if(result != null){
            if(result.contents != null){
                val str = result.contents.split(" ")
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("bank",str[0])
                intent.putExtra("code",str[1])
                startActivity(intent)
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
        if(result.size!=0)
            btnsXML.visibility = View.VISIBLE
        accountList.add(account((-2).toLong(),"","",makeQR("")))
    }

    private fun setUI(){
        val accountAdapter = Adpater(this, accountList, viewPagerXML)

        viewPagerXML.adapter = accountAdapter
        viewPagerXML.addOnPageChangeListener(object: OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                if(accountList[position].ID==(-2).toLong()){
                    btnsXML.visibility = View.INVISIBLE
                }else{
                    btnsXML.visibility = View.VISIBLE
                }
            }
        })
        editXML.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("ID",accountList[viewPagerXML.currentItem].ID)
            intent.putExtra("bank",accountList[viewPagerXML.currentItem].bank)
            intent.putExtra("code",accountList[viewPagerXML.currentItem].code)
            startActivity(intent)
        }
        deleteXML.setOnClickListener {
            val intent = Intent(this, DeleteActivity::class.java)
            intent.putExtra("ID",accountList[viewPagerXML.currentItem].ID)
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
}

