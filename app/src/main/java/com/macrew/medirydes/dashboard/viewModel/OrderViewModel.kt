package com.macrew.medirydes.dashboard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.exception.ErrorHandler
import com.macrew.medirydes.login.model.login.LoginModel
import com.macrew.medirydes.retrofit.ApiClient
import com.macrew.medirydes.retrofit.WebResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel  : ViewModel() {
    var responseDashboardData: MutableLiveData<WebResponse<LoginModel>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()

    var errorHandler: ErrorHandler? = ErrorHandler()


    /*
     * method to call login api
     * */
    fun callGetLoginApi(email: String, password: String, company_id: String) {
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
                        responseDashboardData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseDashboardData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseDashboardData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(
                call: Call<LoginModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseDashboardData.setValue(WebResponse(Status.FAILURE, null, errorMsg))
            }
        })
    }


}