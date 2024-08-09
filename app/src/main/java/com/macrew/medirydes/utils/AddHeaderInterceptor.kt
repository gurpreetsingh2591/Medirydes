package com.macrew.medirydes.utils

import com.macrew.medirydes.annotation.Constants
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

class AddHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //Basic Auth
        val builder = chain.request().newBuilder()
        builder.addHeader(Constants.AUTH, Constants.BEARER+SharedPrefrencesUtils.getUserToken().toString())
        return chain.proceed(builder.build())
    }
}