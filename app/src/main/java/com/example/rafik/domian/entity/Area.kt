package com.example.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Area(
    @SerializedName("arName")
    val arName: String = "",
    @SerializedName("enName")
    val enName: String = "",) {
    override fun toString(): String {
        return "$enName:$arName"
    }
}