package com.example.qr
import android.content.Context
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
        val qr = view.findViewById<ImageView>(R.id.qr)
        val bank = view.findViewById<TextView>(R.id.bank)
        val code = view.findViewById<TextView>(R.id.code)
        val account = accountList[position]

        qr.setImageBitmap(account.QR)
        bank.text = account.bank
        code.text = account.code
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
