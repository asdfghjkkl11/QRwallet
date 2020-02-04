package com.example.qr

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmModel(
    @PrimaryKey var ID: Long = 0,
    var bank: String = "",
    var code: String = ""
): RealmObject(){}

data class ResponseModel (
    @SerializedName("resultType")
    var resultType: String = "",
    @SerializedName("success")
    var success: RepoModel,
    @SerializedName("fail")
    var fail: String = ""
)

data class RepoModel(
    @SerializedName("link")
    var link: String = "",
    @SerializedName("scheme")
    var scheme: String = ""
)

data class RequestModel(
    @SerializedName("apiKey")
    var apiKey: String,
    @SerializedName("bankName")
    var bankName: String,
    @SerializedName("bankAccountNo")
    var bankAccountNo: String
)