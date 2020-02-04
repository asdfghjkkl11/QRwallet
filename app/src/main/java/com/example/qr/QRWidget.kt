package com.example.qr

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRWidget : AppWidgetProvider() {
    private var inputValues: Bitmap? = null
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        val remoteViews = RemoteViews(context.packageName,R.layout.q_r_widget)
        val qr = intent.getStringExtra("QR")
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE==action && qr != null) {
            inputValues= makeQR(qr)
            updateQRcode(context,remoteViews)
        }
    }

    private fun updateQRcode(context: Context, remoteViews: RemoteViews) {
        remoteViews.setImageViewBitmap(R.id.imageView,inputValues)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context,QRWidget::class.java))
        if(appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            appWidgetManager.updateAppWidget(appWidgetIds,remoteViews)
            this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


fun makeQR(text:String): Bitmap? {
    val multiFormatWriter = MultiFormatWriter()
    try {
        val barcodeManager = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,800,800)
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(barcodeManager)
    }catch (e:Exception){}
    return null
}