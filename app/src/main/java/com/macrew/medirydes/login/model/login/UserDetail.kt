package com.macrew.medirydes.login.model.login

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("gender") val gender: String?,
    @SerializedName("age") val age: String?,
    @SerializedName("image_file_name") val image_file_name: String?,
    @SerializedName("image_file_size") val image_file_size: String?,
    @SerializedName("image_content_type") val image_content_type: String?,
    @SerializedName("image_updated_at") val image_updated_at: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("license_number") val license_number: String?,
    @SerializedName("license_expire") val license_expire: String?,
    @SerializedName("image_url") val image_url: String?
)