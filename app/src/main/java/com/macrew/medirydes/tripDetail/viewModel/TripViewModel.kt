package com.macrew.medirydes.tripDetail.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.exception.ErrorHandler
import com.macrew.medirydes.login.model.login.LoginModel
import com.macrew.medirydes.retrofit.ApiClient
import com.macrew.medirydes.retrofit.WebResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripViewModel  : ViewModel() {
    var responseStartTripData: MutableLiveData<WebResponse<DefaultModel>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()

    var errorHandler: ErrorHandler? = ErrorHandler()


    /*
     * method to call login api
     * */
    fun callGetStartTripApi(id: String,  status: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getStartTrip(id,"PUT",status)
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
                        responseStartTripData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseStartTripData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseStartTripData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(
                call: Call<DefaultModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseStartTripData.setValue(WebResponse(Status.FAILURE, null, errorMsg))
            }
        })
    }


}