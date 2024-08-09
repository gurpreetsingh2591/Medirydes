package com.macrew.medirydes.requestTimeOff.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.avalibilty.WorkDay
import com.macrew.medirydes.exception.ErrorHandler
import com.macrew.medirydes.retrofit.ApiClient
import com.macrew.medirydes.retrofit.WebResponse
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAvailabilityViewModel : ViewModel() {
    var responseEditAvailabilityData: MutableLiveData<WebResponse<DefaultModel>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()

    var errorHandler: ErrorHandler? = ErrorHandler()


    /*
     * method to call login api
     * */
    fun callGetEditAvailabilityApi(string: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)


        val call = ApiClient().getClient().getEditAvailability(string)
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
                        responseEditAvailabilityData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseEditAvailabilityData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseEditAvailabilityData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(
                call: Call<DefaultModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseEditAvailabilityData.setValue(WebResponse(Status.FAILURE, null, errorMsg))
            }
        })
    }
}