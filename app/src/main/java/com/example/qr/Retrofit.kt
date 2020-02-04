package com.example.qr

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("/transfer-web/linkgen-api/link")
    fun postJson(@Body body: RequestModel): Call<ResponseModel>
}

fun getService():RetrofitService{

    val client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(newRequest)
    }.build()

    val retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://toss.im")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    return retrofit.create(RetrofitService::class.java)
}