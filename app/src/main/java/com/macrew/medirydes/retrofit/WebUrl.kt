package com.macrew.medirydes.retrofit

interface WebUrl {
    companion object {

        //Admin API's
       // const val API_LOGIN = "admin/admin_login"
        const val API_LOGIN = "login"
        const val API_VERIFY_COMPANY= "checkcompany"
        const val API_FORGOT_PASSWORD = "forgot_password"
        const val API_GET_DASHBOARD = "users/schedules"
        const val API_START_SCHEDULE = "schedules"
        const val API_GET_FUTURE="users/futureschedules"
        const val API_GET_REPORTS="users/historyschedules"
        const val JSON_GET_INSPECTION_LIST="inspectionList.json"
        const val API_GET_INSPECTION_DETAIL="inspections"
        const val API_POST_REARRANGE_LIST="tripsorting"
        const val API_GET_EDIT_PROFILE="editprofile"
        const val API_GET_REQUEST_TIME_OFF="users/request_time_off"
        const val API_GET_EDIT_REQUEST_TIME="users/edit_request_time"
        const val API_GET_START_TRIP="trips"
        const val API_GET_AVAILABILITY="availability"
        const val API_POST_AVAILABILITY="availability"



        //com.macrew.medirydes.login.model.login.com.macrew.medirydes.dashboard.model.currentSchedule.com.macrew.medirydes.dashboard.model.abc.com.macrew.medirydes.dashboard.model.profile.User API's
        const val API_USER_LOGIN = "login"

    }

}