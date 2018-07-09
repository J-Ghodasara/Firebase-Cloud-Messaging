package com.example.jayghodasara.pushnotification

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RetrofitCallInterface{

    @POST("send")
    fun sendreq(
            @HeaderMap  headers:Map<String,String>,
            @Body fcm:FCM
    ):Call<ResponseBody>
}