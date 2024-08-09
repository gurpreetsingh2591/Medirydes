package com.macrew.medirydes.login.model.login

import com.google.gson.annotations.SerializedName

data class Company(
	@SerializedName("id") val id: Int?,
	@SerializedName("company_code") val company_code: String?,
	@SerializedName("company_name") val company_name: String?,
	@SerializedName("phone") val phone: String?,
	@SerializedName("address") val address: String?,
	@SerializedName("created_at") val created_at: String?,
	@SerializedName("updated_at") val updated_at: String?
)