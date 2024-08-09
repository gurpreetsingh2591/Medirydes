package com.macrew.medirydes.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.macrew.medirydes.utils.SharedPrefrencesUtils

class App : Application() {
    private var currentApplication: App? = null
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
        SharedPrefrencesUtils.init(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //  val appComponent = DaggerApplicationComponent.create()

    }

    fun getInstance(): App? {

        return currentApplication
    }

//    fun getWebService(): ApiInterface? {
//        val token = "Basic TExVU0VSOiZmNzJHZS15PWhzO1pAYDM="/*SharedPreferencesUtils.getInstance(currentApplication?.getInstance()!!).getValue(Constants.KEY_ACCESS_TOKEN, "")*/
//        return ServiceGenerator.createService(ApiInterface::class.java, token)
//    }

//    fun getWebService(): ApiInterface? {
//        val token: String = SharedPreferencesUtils.getInstance(currentApplication?.getInstance()!!).getValue(Constants.KEY_ACCESS_TOKEN, "").toString()
//        return ServiceGenerator.createService(ApiInterface::class.java, token)
//    }

}