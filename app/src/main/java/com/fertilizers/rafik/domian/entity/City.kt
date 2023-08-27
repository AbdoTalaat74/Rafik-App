package com.fertilizers.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class City(
    @SerializedName("arName")
    val arName: String = "",
    @SerializedName("enName")
    val enName: String = "",
    @SerializedName("areas")
    val areas: List<Area> = listOf()
) {
    override fun toString(): String {
        return "$enName:$arName"
    }
}