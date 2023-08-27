package com.fertilizers.rafik.domian.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("uid")
    var uid: String = "",
    @SerializedName("city")
    var city: City = City(),
    @SerializedName("area")
    var area: Area = Area(),
) {
    override fun toString(): String {
        return "name =$name ,phone=$phone ,address=$address"
    }
}