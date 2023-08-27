package com.fertilizers.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CropType(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("isAvailable")
    val isAvailable: Boolean = false,
    @SerializedName("uid")
    var uid: String = ""
) {
    override fun toString(): String {
        return name
    }
}