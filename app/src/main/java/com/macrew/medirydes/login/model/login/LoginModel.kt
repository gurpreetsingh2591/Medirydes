package com.macrew.medirydes.login.model.login


import com.google.gson.annotations.SerializedName


data class LoginModel(
    @SerializedName("StatusCode") val StatusCode: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("result") val result: ResultModel?
)