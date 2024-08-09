package com.macrew.medirydes.login.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.exception.ErrorHandler
import com.macrew.medirydes.extra.WebResponse
import com.macrew.medirydes.login.model.login.LoginModel
import com.macrew.medirydes.login.model.company.CompanyModel
import com.macrew.medirydes.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var responseLoginData: MutableLiveData<WebResponse<LoginModel>> = MutableLiveData()
    var responseCompanyData: MutableLiveData<WebResponse<CompanyModel>> = MutableLiveData()
    var responseForgotData: MutableLiveData<WebResponse<DefaultModel>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()

    var errorHandler: ErrorHandler? = ErrorHandler()


    /*
     * method to call login api
     * */
    fun callGetLoginApi(email: String, password: String,company_id: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)


        val call = ApiClient().getClient().getLogin(email,password,company_id)
            call.enqueue(object : Callback<LoginModel?> {
                override fun onResponse(
                    call: Call<LoginModel?>,
                    response: Response<LoginModel?>
                ) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                    if (response.isSuccessful && response.body() != null) {
                        val result: LoginModel? = response.body()
                        if (result?.StatusCode =="200")
                            responseLoginData.setValue(
                                WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                            responseLoginData.setValue(
                                WebResponse(Status.FAILURE, null, response.body()!!.message)
                            )
                        }
                    } else {
                        val errorMsg = errorHandler!!.reportError(response.code())
                        responseLoginData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                    }
                }

                override fun onFailure(
                    call: Call<LoginModel?>,
                    t: Throwable
                ) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                     val errorMsg = errorHandler!!.reportError(t)
                    responseLoginData.setValue(WebResponse(Status.FAILURE, null, errorMsg))
                }
            })
    }

    fun callGetCompanyVerifyApi(companyCode: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)
        val call = ApiClient().getClient().getCompanyVerify(companyCode)
        call.enqueue(object : Callback<CompanyModel?> {
            override fun onResponse(
                call: Call<CompanyModel?>,
                response: Response<CompanyModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: CompanyModel? = response.body()
                    if (result?.StatusCode ==200)
                        responseCompanyData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseCompanyData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseCompanyData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(
                call: Call<CompanyModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseCompanyData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }

    fun callGetForgotPasswordApi(email: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)


        val call = ApiClient().getClient().getForgotPassword(email)
        call.enqueue(object : Callback<DefaultModel?> {
            override fun onResponse(
                call: Call<DefaultModel?>,
                response: Response<DefaultModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: DefaultModel? = response.body()
                    if (result?.StatusCode =="200")
                        responseForgotData.setValue(
                           WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseForgotData.setValue(
                           WebResponse(
                                Status.FAILURE,
                                null,
                                response.body()!!.message
                            )
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseLoginData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<DefaultModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseForgotData.value = WebResponse(
                    Status.FAILURE,
                    null,
                    errorMsg
                )
            }
        })
    }

}