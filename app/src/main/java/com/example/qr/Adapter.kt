package com.example.qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class Adpater (
    private val context: Context,
    private val accountList: ArrayList<account>,
    private val viewPager: ViewPager
) : PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.account, null)
        val qr = view.findViewById<ImageView>(R.id.qrXML)
        val bank = view.findViewById<TextView>(R.id.bankXML)
        val code = view.findViewById<TextView>(R.id.codeXML)
        val text = view.findViewById<TextView>(R.id.textXML)
        val account = accountList[position]
        if(account.ID == (-2).toLong()) {
            bank.visibility = View.GONE
            code.visibility = View.GONE
            qr.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java)
                intent.putExtra("ID",(-1).toLong())
                val activity = context as Activity
                activity.startActivity(intent)
            }
        }else {
            text.visibility = View.GONE
            qr.setImageBitmap(account.QR)
            qr.setOnClickListener {
                val bank = account.bank
                val code = account.code
                val intent = Intent(context, QRPopupActivity::class.java)
                intent.putExtra("bank", bank)
                intent.putExtra("code", code)
                val activity = context as Activity
                activity.startActivity(intent)
            }
            bank.text = account.bank
            code.text = account.code
        }
        viewPager.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        viewPager.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return accountList.size
    }
}
