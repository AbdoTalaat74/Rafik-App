package com.example.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City (
    @SerializedName("cityName")
    val cityName: String = "",
    @SerializedName("uid")
    var uid: String = ""
){
    override fun toString(): String {
        return cityName
    }
}