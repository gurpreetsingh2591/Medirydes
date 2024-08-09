package com.macrew.medirydes.login.model.login

import com.google.gson.annotations.SerializedName

data class User(
	@SerializedName("id") val id: Int?,
	@SerializedName("name") val name: String?,
	@SerializedName("username") val username: String?,
	@SerializedName("email") val email: String?,
	@SerializedName("phone") val phone: String?,
	@SerializedName("email_verified_at") val email_verified_at: String?,
	@SerializedName("is_phone_verified") val is_phone_verified: String?,
	@SerializedName("status") val status: String?,
	@SerializedName("device_token") val device_token: String?,
	@SerializedName("isd_code") val isd_code: String?,
	@SerializedName("last_login") val last_login: String?,
	@SerializedName("total_login") val total_login: Int,
	@SerializedName("created_at") val created_at: String?,
	@SerializedName("updated_at") val updated_at: String?,
	@SerializedName("deleted_at") val deleted_at: String?,
	@SerializedName("token") val token: String?,
	@SerializedName("user_detail") val user_detail: UserDetail?
)