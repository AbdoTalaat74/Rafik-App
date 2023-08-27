package com.fertilizers.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Area(
    @SerializedName("arName")
    val arName: String = ""
) {
    override fun toString(): String {
        return arName
    }
}