package com.macrew.medirydes.login.model.login

import com.google.gson.annotations.SerializedName

data class ResultModel(
	@SerializedName("user") val user: User?,
	@SerializedName("company") val company: Company?
)