package com.macrew.medirydes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.macrew.medirydes.annotation.Constants
import okhttp3.internal.cache.DiskLruCache

object SharedPrefrencesUtils {

    private const val SHARED_PREFERENCES = Constants.PREFERENCE_NAME
    private var sPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private const val IS_USER_LOGIN = Constants.IS_USER_LOGIN
    private const val USER_ID = Constants.USER_ID
    private const val TOKEN = Constants.TOKEN
    private const val ROLE = Constants.ROLE
    private const val STATUS = Constants.STATUS

    private const val COMPANY_ID = Constants.COMPANY_ID
    private const val COMPANY_CODE = Constants.COMPANY_CODE
    private const val COMPANY_NAME = Constants.COMPANY_NAME
    private const val COMPANY_PHONE = Constants.COMPANY_PHONE
    private const val COMPANY_ADDRESS = Constants.COMPANY_ADDRESS
    private const val VEHICLE_ID = Constants.VEHICLE_ID

    private const val USER_NAME = Constants.USER_NAME
    private const val USER_EMAIL = Constants.USER_EMAIL
    private const val USER_PHONE = Constants.USER_PHONE
    private const val Address = Constants.Address
    private const val DOB = Constants.DOB
    private const val Country = Constants.Country
    private const val State = Constants.State
    private const val City = Constants.City
    private const val Pin = Constants.Pin
    private const val Gender = Constants.Gender
    private const val AGE = Constants.AGE
    private const val LICENCE_NO = Constants.LICENCE_NO
    private const val IMAGE = Constants.IMAGE
    private const val FcmToken = Constants.FcmToken
    private const val DeviceId = Constants.DeviceId
    private const val transport_id = Constants.TRANSPORT_ID
    private const val Current_lat = Constants.LAT
    private const val Current_long = Constants.LONG
    private const val MOBILITY_TYPE= Constants.MOBILITY

    @SuppressLint("CommitPrefEdits")
    fun init(context: Context?) {
        sPreferences = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        editor = sPreferences!!.edit()
    }

    //get is user login?
    fun isUserLogin(): Boolean? {
        return sPreferences?.getBoolean(IS_USER_LOGIN, false)
    }

    //get user detail
    fun getUserId(): String? {
        return sPreferences?.getString(USER_ID, "")
    }

    fun getUserName(): String? {
        return sPreferences?.getString(USER_NAME, "")
    }

    fun getUserEmail(): String? {
        return sPreferences?.getString(USER_EMAIL, "")
    }

    fun getUserNumber(): String? {
        return sPreferences?.getString(USER_PHONE, "")
    }

    fun getUserImage(): String? {
        return sPreferences?.getString(IMAGE, "")
    }

    fun getUserToken(): String? {
        return sPreferences?.getString(TOKEN, "")
    }

    fun getAddress(): String? {
        return sPreferences?.getString(Address, "")
    }

    fun getCountry(): String? {
        return sPreferences?.getString(Country, "")
    }

    fun getState(): String? {
        return sPreferences?.getString(State, "")
    }

    fun getCity(): String? {
        return sPreferences?.getString(City, "")
    }

    fun getDob(): String? {
        return sPreferences?.getString(DOB, "")
    }

    fun getAge(): String? {
        return sPreferences?.getString(AGE, "")
    }

    fun getGender(): String? {
        return sPreferences?.getString(Gender, "")
    }

    fun getPin(): String? {
        return sPreferences?.getString(Pin, "")
    }

    fun getDeviceId(): String? {
        return sPreferences?.getString(DeviceId, "")
    }

    fun getFcmToken(): String? {
        return sPreferences?.getString(FcmToken, "")
    }


    //get com.macrew.medirydes.login.model.login.Company detail
    fun getCompanyCode(): String? {
        return sPreferences?.getString(COMPANY_CODE, "")
    }

    fun getCompanyName(): String? {
        return sPreferences?.getString(COMPANY_NAME, "")
    }
    fun getLicenceNo(): String? {
        return sPreferences?.getString(LICENCE_NO, "")
    }

    fun getCompanyPhone(): String? {
        return sPreferences?.getString(COMPANY_PHONE, "")
    }

    fun getCompanyAddress(): String? {
        return sPreferences?.getString(COMPANY_ADDRESS, "")
    }

    fun getCompanyId(): String? {
        return sPreferences?.getString(COMPANY_ID, "")
    }

    //get current lat long
    fun getCurrentLat(): String? {
        return sPreferences?.getString(Current_lat, "")
    }

    fun getCurrentLong(): String? {
        return sPreferences?.getString(Current_long, "")
    }


    fun getTransId(): String? {
        return sPreferences?.getString(transport_id, "")
    }

    fun getRole(): String? {
        return sPreferences?.getString(ROLE, "")
    }

    fun getStatus(): String? {
        return sPreferences?.getString(STATUS, "")

    }
    fun getMobilityType(): String? {
        return sPreferences?.getString(MOBILITY_TYPE, "")

    }
    // set user and company detail

    fun setFcmToken(token: String) {
        sPreferences?.edit()
            ?.putString(FcmToken, token)
            ?.apply()
    }
    fun setMobilityType(type: String) {
        sPreferences?.edit()
            ?.putString(MOBILITY_TYPE, type)
            ?.apply()
    }

    fun setDeviceId(device: String) {
        sPreferences?.edit()
            ?.putString(DeviceId, device)
            ?.apply()
    }

    fun setCurrentLat(lat: String) {
        sPreferences?.edit()
            ?.putString(Current_lat, lat)
            ?.apply()
    }

    fun setCurrentLong(long: String) {
        sPreferences?.edit()
            ?.putString(Current_long, long)
            ?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setCompanyDetail(
        id: String?,
        company_name: String?,
        company_code: String?,
        company_phone: String?,
        company_address: String?,
        vehicle_id: String?,

        ) {
        sPreferences?.edit()
            ?.putString(COMPANY_ID, id)
            ?.putString(COMPANY_NAME, company_name)
            ?.putString(COMPANY_CODE, company_code)
            ?.putString(COMPANY_PHONE, company_phone)
            ?.putString(COMPANY_ADDRESS, company_address)
            ?.putString(VEHICLE_ID, vehicle_id)
            ?.apply()
    }


    fun setUserLogin(
        status: Boolean,
        id: String?,
        name: String?,
        email: String?,
        phone: String?,
        token: String?,
        gender: String?,
        age: String?,
        licenceNo: String?,
        image_file_name: String?

    ) {
        sPreferences?.edit()
            ?.putBoolean(IS_USER_LOGIN, status)
            ?.putString(USER_ID, id)
            ?.putString(USER_NAME, name)
            ?.putString(USER_EMAIL, email)
            ?.putString(USER_PHONE, phone)
            ?.putString(TOKEN, token)
            ?.putString(Gender, gender)
            ?.putString(AGE, age)
            ?.putString(LICENCE_NO, licenceNo)
            ?.putString(IMAGE, image_file_name)
            ?.apply()
    }

    fun setUserLogin(
        name: String?,
        email: String?,
        phone: String?,
        token: String?,
        gender: String?,
        age: String?,
        image_file_name: String?

    ) {
        sPreferences?.edit()
            ?.putString(USER_NAME, name)
            ?.putString(USER_EMAIL, email)
            ?.putString(USER_PHONE, phone)
            ?.putString(TOKEN, token)
            ?.putString(Gender, gender)
            ?.putString(AGE, age)
            ?.putString(IMAGE, image_file_name)
            ?.apply()
    }


    fun clearUser() {
        sPreferences?.edit()
            ?.clear()
            ?.apply()
    }


}