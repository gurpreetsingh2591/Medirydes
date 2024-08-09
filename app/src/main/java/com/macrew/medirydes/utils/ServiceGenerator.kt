package com.macrew.medirydes.utils

import android.util.Log
import com.google.gson.GsonBuilder
import com.macrew.medirydes.annotation.Constants
import com.macrew.medirydes.application.App
import com.macrew.medirydes.exception.NetworkUtil.Companion.isNetAvail
import com.macrew.medirydes.exception.NoInternetException
import com.macrew.medirydes.retrofit.Constant
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

/**
 * Retrofit service generator to call API
 */
class ServiceGenerator  // No need to instantiate this class.
private constructor() {
    var token = ""

    class MyOkHttpInterceptor internal constructor(token: String?) : Interceptor {
        var token: String? = ""

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            var newRequest: Request? = null
            newRequest = if (token != null) {
                if (token!!.isNotEmpty()) {
                    originalRequest.newBuilder()
                        .header(Constants.AUTH, "Bearer $token")
                        .build()
                } else originalRequest.newBuilder()
                    .build()
            } else originalRequest.newBuilder().build()
            return chain.proceed(newRequest)
        }

        init {
            this.token = token
        }
    }

    companion object {
        private const val TAG = Constants.RETROFIT_MANAGER
        val app = App()
        // endregion
        private val gson = GsonBuilder().setLenient().serializeNulls().create()
        private val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
        private val defaultOkHttpClient =
            OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES).build()

        fun <S> createService(serviceClass: Class<S>?): S {
            return createService(
                serviceClass,
                Constant.BASE_URL,
                MyOkHttpInterceptor("")
            )
        }

        fun <S> createService(
            serviceClass: Class<S>?,
            baseUrl: String?,
            no: String?
        ): S {
            return createService(
                serviceClass,
                baseUrl,
                MyOkHttpInterceptor(no)
            )
        }

        fun <S> createService(serviceClass: Class<S>?, token: String?): S {
            return createService(
                serviceClass,
                Constant.BASE_URL,
                MyOkHttpInterceptor(token)
            )
        }

        private fun <S> createService(
            serviceClass: Class<S>?,
            baseUrl: String?,
            networkInterceptor: Interceptor?
        ): S {
            val okHttpClientBuilder =
                defaultOkHttpClient.newBuilder()
            if (networkInterceptor != null) {
                okHttpClientBuilder.addNetworkInterceptor(networkInterceptor)
            }
            okHttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(90, TimeUnit.SECONDS)
            val modifiedOkHttpClient: OkHttpClient =
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(Companion.networkInterceptor).build()
            // Install the all-trusting trust manager
            retrofitBuilder.client(modifiedOkHttpClient)
            retrofitBuilder.baseUrl(baseUrl!!)
            val retrofit = retrofitBuilder.build()
            return retrofit.create(serviceClass!!)
        }




        // check internet available or not
        private val networkInterceptor: Interceptor
            get() = Interceptor { chain: Interceptor.Chain ->
                val isConnected =
                    isNetAvail(app.getInstance())
                if (!isConnected) {
                    throw NoInternetException() // Throwing our custom exception 'InternetException'
                }
                val builder = chain.request().newBuilder()
                chain.proceed(builder.build())
            }

        // 10 MB
        private val cache: Cache?
            private get() {
                val app: App = App()
                var mCache: Cache? = null
                try {
                    mCache = Cache(
                        File(app.getInstance()!!.cacheDir, Constants.HTTP_CACHE),
                        10 * 1024 * 1024
                    ) // 10 MB
                } catch (e: Exception) {
                    Log.e(TAG, Constants.COULD_NOT_CACHE)
                }
                return mCache
            }

        //        if (BuildConfig.DEBUG) {
        //        } else {
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
        private val httpLoggingInterceptor: Interceptor
            private get() {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                //        if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                //        } else {
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
                return httpLoggingInterceptor
            }
    }
}