package com.macrew.medirydes.retrofit


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.macrew.medirydes.dashboard.model.DefaultModel
import com.macrew.medirydes.dashboard.model.avalibilty.AvailabilityModel
import com.macrew.medirydes.dashboard.model.avalibilty.WorkDay
import com.macrew.medirydes.dashboard.model.currentSchedule.CurrentScheduleModel
import com.macrew.medirydes.dashboard.model.future.FutureModel
import com.macrew.medirydes.dashboard.model.profile.ProfileModel
import com.macrew.medirydes.dashboard.model.reports.ReportsModel
import com.macrew.medirydes.login.model.company.CompanyModel
import com.macrew.medirydes.login.model.login.LoginModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    // login api
    @FormUrlEncoded
    @POST(WebUrl.API_LOGIN)
    fun getLogin(
        @Field(Constant.EMAIL) email: String,
        @Field(Constant.PASSWORD) password: String,
        @Field(Constant.COMPANY_ID) company_id: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST(WebUrl.API_VERIFY_COMPANY)
    fun getCompanyVerify(
        @Field(Constant.COMPANY_CODE) email: String,
    ): Call<CompanyModel>

    @FormUrlEncoded
    @POST(WebUrl.API_FORGOT_PASSWORD)
    fun getForgotPassword(
        @Field(Constant.EMAIL) email: String,
    ): Call<DefaultModel>

    // get dashboard data
    @GET(WebUrl.API_GET_DASHBOARD)
    fun getDashboardData(
    ): Call<CurrentScheduleModel>

    // get future data
    @GET(WebUrl.API_GET_FUTURE)
    fun getFutureData(
    ): Call<FutureModel>

    // get future data
    @GET(WebUrl.API_GET_REPORTS)
    fun getReportData(
        @Query(Constant.FROM_DATE) fromDate: String,
        @Query(Constant.TO_DATE) toDate: String,
    ): Call<ReportsModel>


    //get start Schedule
    @FormUrlEncoded
    @POST(WebUrl.API_START_SCHEDULE+"/{id}")
    fun getStartSchedule(
        @Path("id") id: String?,
        @Field(Constant.METHOD) method: String,
        @Field(Constant.STATUS) status: String
    ): Call<CurrentScheduleModel>

    //get Complete Schedule
    @FormUrlEncoded
    @POST(WebUrl.API_GET_DASHBOARD+"/{id}")
    fun getCompleteSchedule(
        @Path("id") id: String?,
        @Field(Constant.METHOD) method: String,
        @Field(Constant.STATUS) status: String
    ): Call<DefaultModel>

    //post Edit Profile
    @Multipart
    @POST(WebUrl.API_GET_EDIT_PROFILE)
    fun getEditProfile(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<ProfileModel>

    @Multipart
    @POST(WebUrl.API_GET_EDIT_PROFILE)
    fun getEditProfileWithoutImage(
        @PartMap partMap: HashMap<String, RequestBody>,
    ): Call<ProfileModel>


    //get send inspection list
    @FormUrlEncoded
    @POST(WebUrl.API_GET_INSPECTION_DETAIL)
    fun getSendInspectionList(
        @Field(Constant.SCHEDULE_ID) schedule_id: String,
        @Field(Constant.VEHICLE_ID) vehicle_id: String,
        @Field(Constant.CHECKUPS) checkups: HashMap<String?,String?>,
        @Field(Constant.SHIFT) shift: String
    ): Call<DefaultModel>

    //get send inspection list
    @FormUrlEncoded
    @POST(WebUrl.API_POST_REARRANGE_LIST)
    fun getSendReArrangeList(
        @Field("ids[]") list:ArrayList<String>
    ): Call<CurrentScheduleModel>

    //get send inspection list
    @FormUrlEncoded
    @POST(WebUrl.API_POST_REARRANGE_LIST)
    fun getSendReArrangeListFuture(
        @Field("ids[]") list:ArrayList<String>
    ): Call<FutureModel>

    // get Inspection Json Data
    @GET(WebUrl.JSON_GET_INSPECTION_LIST)
    fun getInspectionJsonData(
    ): Call<JsonArray>

    // get Availability Data
    @GET(WebUrl.API_GET_AVAILABILITY)
    fun getAvailabilityData(
    ): Call<AvailabilityModel>


    //get start Schedule
    @FormUrlEncoded
    @POST(WebUrl.API_GET_START_TRIP+"/{id}")
    fun getStartTrip(
        @Path("id") id: String?,
        @Field(Constant.METHOD) method: String,
        @Field(Constant.STATUS) status: String
    ): Call<DefaultModel>


    //get start Schedule
    @FormUrlEncoded
    @POST(WebUrl.API_POST_AVAILABILITY)
    fun getEditAvailability(
        @Field("work_days") status: String,
    ): Call<DefaultModel>

//    //get batch data
//    @GET(WebUrl.API_GET_BATCH)
//    fun getBatchData(
//        @Query(Constant.PAGE) page: Int,
//        @Query(Constant.TROUT_FARM) troutFarm: String,
//        @Query(Constant.BATCH_NO) batchNo: String,
//        @Query(Constant.WEEK_READY) weeklyReady: String,
//        @Query(Constant.INTEVAL) filterId: String
//    ): Call<BatchModel>
//
//    //get order data
//    @GET(WebUrl.API_GET_ORDER)
//    fun getOrderData(
//        @Query(Constant.PAGE) page: Int,
//        @Query(Constant.COMPANY) troutFarm: String,
//        @Query(Constant.CUSTOMER_ID) customer_id: String,
//        @Query(Constant.DELIVERY_WEEK) weeklyReady: String,
//        @Query(Constant.INTEVAL) filterID: String
//    ): Call<OrderModel>
//
//    //get spinner data
//    @GET(WebUrl.API_GET_SPINNER_DATA)
//    fun getSpinnerData(
//    ): Call<SpinnerModel>
//
//    //get batch detail
//    @GET(WebUrl.API_GET_BATCH_DETAIL)
//    fun getBatchDetail(
//        @Query(Constant.ID) id: String
//    ): Call<BatchDetailModel>
//
//    //delete batch
//    @FormUrlEncoded
//    @POST(WebUrl.API_GET_BATCH_DELETE)
//    fun postDeleteBatch(
//        @Field(Constant.ID) id: String
//    ): Call<DefaultModel>
//
//    // Get calculation data
//    @FormUrlEncoded
//    @POST(WebUrl.API_GET_CALCULATION)
//    fun getCalculationData(
//        @Field(Constant.FARM_ID) farmId: String,
//        @Field(Constant.PRODUCTION_DATE) productionDate: String,
//        @Field(Constant.SHIPMENT_DATE) shipmentDate: String,
//        @Field(Constant.T2_DATE) t2Date: String
//    ): Call<CalculationDataModel>
//
//
//    // Post Add Batch data
//    @FormUrlEncoded
//    @POST(WebUrl.API_GET_ADD_BATCH)
//    fun getAddBatchData(
//        @FieldMap batchData: HashMap<String, String>
//    ): Call<DefaultModel>
//
//    // Post update Batch data
//    @FormUrlEncoded
//    @POST(WebUrl.API_GET_UPDATE_BATCH)
//    fun getUpdateBatchData(
//        @FieldMap batchData: HashMap<String, String>
//    ): Call<DefaultModel>
//
//    //Order detail
//    @GET(WebUrl.API_GET_ORDER_DETAIL)
//    fun getOrderDetail(
//        @Query(Constant.ID) id: String
//    ): Call<OrderDetailModel>
//
//    //delete order
//    @FormUrlEncoded
//    @POST(WebUrl.API_GET_ORDER_DELETE)
//    fun postDeleteOrder(
//        @Field(Constant.ID) id: String
//    ): Call<DefaultModel>
//
//
//    //get batch number
//    @GET(WebUrl.API_GET_BATCH_NUMBER)
//    fun getBatchNumber(
//        @Query(Constant.ID) id: String
//    ): Call<BatchNumberModel>
//
//
//    //get contact person
//    @GET(WebUrl.API_GET_CONTACT_PERSON)
//    fun getContactPerson(
//        @Query(Constant.COMPANY_ID) id: String
//    ): Call<ContactPersonModel>
//
//
//    //checklist
//    @GET(WebUrl.API_GET_CHECK_LIST)
//    fun getChecklistDetail(
//    ): Call<JsonObject>
//
//
//    //Add Order
//    @Multipart
//    @POST(WebUrl.API_GET_ADD_ORDER)
//    fun postAddOrder(
//        @PartMap partMap: MutableMap<String, RequestBody>,
//        @Part ("box[]") list:ArrayList<RequestBody>,
//        @Part files: List<MultipartBody.Part>
//    ): Call<DefaultModel>
//
//    //Edit Order
//    @Multipart
//    @POST(WebUrl.API_GET_EDIT_ORDER)
//    fun postEditOrder(
//        @PartMap partMap: HashMap<String, RequestBody>,
//        @Part ("box[]") list:ArrayList<RequestBody>,
//        @Part files: List<MultipartBody.Part>
//    ): Call<DefaultModel>
//
//    //get address pdf
//    @GET(WebUrl.API_GET_ORDER_ADDRESS_PDF)
//    fun getOrderAddressPdf(
//        @Query(Constant.ID) id: String
//    ): Call<PdfModel>
//
//    //get address pdf
//    @GET(WebUrl.API_GET_ORDER_INFO_PDF)
//    fun getOrderInfoPdf(
//        @Query(Constant.ID) id: String
//    ): Call<PdfModel>
//
//    //get address pdf
//    @GET(WebUrl.API_GET_ORDER_PDF)
//    fun getOrderConfirmationPdf(
//        @Query(Constant.ID) id: String
//    ): Call<PdfModel>

}