package com.macrew.medirydes.dashboard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.avalibilty.AvailabilityModel
import com.macrew.medirydes.dashboard.model.currentSchedule.CurrentScheduleModel
import com.macrew.medirydes.dashboard.model.future.FutureModel
import com.macrew.medirydes.dashboard.model.profile.ProfileModel
import com.macrew.medirydes.dashboard.model.reports.ReportsModel
import com.macrew.medirydes.exception.ErrorHandler
import com.macrew.medirydes.retrofit.ApiClient
import com.macrew.medirydes.retrofit.WebResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File


class DashboardViewModel : ViewModel() {
    var responseDashboardData: MutableLiveData<WebResponse<CurrentScheduleModel>> =
        MutableLiveData()
    var responseStartScheduleData: MutableLiveData<WebResponse<CurrentScheduleModel>> =
        MutableLiveData()
    var responseFutureData: MutableLiveData<WebResponse<FutureModel>> = MutableLiveData()
    var responseReportsData: MutableLiveData<WebResponse<ReportsModel>> = MutableLiveData()
    var responseInspectionData: MutableLiveData<WebResponse<JsonArray>> = MutableLiveData()
    var responseInspectionReturnData: MutableLiveData<WebResponse<DefaultModel>> = MutableLiveData()
    var responseEditProfileData: MutableLiveData<WebResponse<ProfileModel>> = MutableLiveData()
    var responseReArrangeListData: MutableLiveData<WebResponse<CurrentScheduleModel>> =
        MutableLiveData()
    var responseReArrangeListFutureData: MutableLiveData<WebResponse<FutureModel>> =
        MutableLiveData()
    var responseAvailabilityData: MutableLiveData<WebResponse<AvailabilityModel>> =
        MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()
    var errorHandler: ErrorHandler? = ErrorHandler()

    /*
     * method to call Dashboard api
     * */
    fun callGetDashboardApi() {
        isLoading.postValue(true)
        isViewEnable.postValue(true)
        val call = ApiClient().getClient().getDashboardData()
        call.enqueue(object : Callback<CurrentScheduleModel?> {
            override fun onResponse(
                call: Call<CurrentScheduleModel?>,
                response: Response<CurrentScheduleModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: CurrentScheduleModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseDashboardData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseDashboardData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseDashboardData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<CurrentScheduleModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseDashboardData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call future api */
    fun callGetFutureListApi() {
        isLoading.postValue(true)
        isViewEnable.postValue(true)


        val call = ApiClient().getClient().getFutureData()
        call.enqueue(object : Callback<FutureModel?> {
            override fun onResponse(
                call: Call<FutureModel?>,
                response: Response<FutureModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: FutureModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseFutureData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseFutureData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseFutureData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<FutureModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseFutureData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call Report api */
    fun callGetReportListApi(from_date: String, to_date: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)


        val call = ApiClient().getClient().getReportData(from_date, to_date)
        call.enqueue(object : Callback<ReportsModel?> {
            override fun onResponse(
                call: Call<ReportsModel?>,
                response: Response<ReportsModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: ReportsModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseReportsData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseReportsData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseReportsData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<ReportsModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseReportsData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call inspection json */
    fun callGetInspectionJson() {
        isLoading.postValue(true)
        isViewEnable.postValue(true)
        val call = ApiClient().getAssetsClient().getInspectionJsonData()
        call.enqueue(object : Callback<JsonArray?> {
            override fun onResponse(
                call: Call<JsonArray?>,
                response: Response<JsonArray?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: JsonArray? = response.body()
                    if (response.isSuccessful)
                        responseInspectionData.setValue(
                            WebResponse(Status.SUCCESS, result, response.message())
                        ) else {
                        responseInspectionData.setValue(
                            WebResponse(Status.FAILURE, null, response.message())
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseInspectionData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<JsonArray?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseInspectionData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call start Schedule api */
    fun callGetStartScheduleApi(id: String, status: String) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getStartSchedule(id, "PUT", status)
        call!!.enqueue(object : Callback<CurrentScheduleModel?> {
            override fun onResponse(
                call: Call<CurrentScheduleModel?>,
                response: Response<CurrentScheduleModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: CurrentScheduleModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseStartScheduleData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseStartScheduleData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseStartScheduleData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<CurrentScheduleModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseReportsData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call edit profile api */
    fun callGetEditProfileApi(
        profileData: HashMap<String,RequestBody>, file : MultipartBody.Part
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getEditProfile(profileData, file)
        call.enqueue(object : Callback<ProfileModel?> {
            override fun onResponse(
                call: Call<ProfileModel?>,
                response: Response<ProfileModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: ProfileModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseEditProfileData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseEditProfileData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseStartScheduleData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<ProfileModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseEditProfileData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    /* method to call edit profile api */
    fun callGetEditProfileWithoutImageApi(
        profileData: HashMap<String,RequestBody>
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getEditProfileWithoutImage(profileData)
        call.enqueue(object : Callback<ProfileModel?> {
            override fun onResponse(
                call: Call<ProfileModel?>,
                response: Response<ProfileModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: ProfileModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseEditProfileData.setValue(
                            WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseEditProfileData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseStartScheduleData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<ProfileModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseEditProfileData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }

    /* method to call start Schedule api */
    fun callGetSendInspectionApi(
        scheduleId: String,
        vehicleId: String,
        checkups: HashMap<String?, String?>,
        shift: String
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call =
            ApiClient().getClient().getSendInspectionList(scheduleId, vehicleId, checkups, shift)
        call.enqueue(object : Callback<DefaultModel?> {
            override fun onResponse(
                call: Call<DefaultModel?>,
                response: Response<DefaultModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: DefaultModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseInspectionReturnData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseInspectionReturnData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseInspectionReturnData.setValue(
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
                responseInspectionReturnData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call send re-arrange list api */
    fun callGetSendReArrangeListApi(
        tripIdsList: ArrayList<String>
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getSendReArrangeList(tripIdsList)
        call.enqueue(object : Callback<CurrentScheduleModel?> {
            override fun onResponse(
                call: Call<CurrentScheduleModel?>,
                response: Response<CurrentScheduleModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: CurrentScheduleModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseReArrangeListData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseReArrangeListData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseReArrangeListData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<CurrentScheduleModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseReArrangeListData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call send re-arrange list api */
    fun callGetSendReArrangeListFutureApi(
        tripIdsList: ArrayList<String>
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getSendReArrangeListFuture(tripIdsList)
        call.enqueue(object : Callback<FutureModel?> {
            override fun onResponse(
                call: Call<FutureModel?>,
                response: Response<FutureModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: FutureModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseReArrangeListFutureData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseReArrangeListFutureData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseReArrangeListFutureData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<FutureModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseReArrangeListFutureData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    /* method to call Availability list api */
    fun callGetAvailabilityListApi(
    ) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        val call = ApiClient().getClient().getAvailabilityData()
        call.enqueue(object : Callback<AvailabilityModel?> {
            override fun onResponse(
                call: Call<AvailabilityModel?>,
                response: Response<AvailabilityModel?>
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: AvailabilityModel? = response.body()
                    if (result?.StatusCode == "200")
                        responseAvailabilityData.setValue(
                            WebResponse(Status.SUCCESS, result, response.body()!!.message)
                        ) else {
                        responseAvailabilityData.setValue(
                            WebResponse(Status.FAILURE, null, response.body()!!.message)
                        )
                    }
                } else {
                    val errorMsg = errorHandler!!.reportError(response.code())
                    responseAvailabilityData.setValue(
                        WebResponse(
                            Status.FAILURE,
                            null,
                            response.message()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<AvailabilityModel?>,
                t: Throwable
            ) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseReArrangeListData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
}