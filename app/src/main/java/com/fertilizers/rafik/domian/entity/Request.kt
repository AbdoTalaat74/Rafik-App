package com.fertilizers.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("area")
    val area: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("uid")
    var uid: String = ""
){
    override fun toString(): String {
        return "username =$username ,phone=$phone ,city=$city ,area=$area ,uid=$uid ,uid=$uid"
    }
}