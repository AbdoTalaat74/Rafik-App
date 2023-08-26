package com.example.rafik.domian.entity

import com.google.firebase.firestore.FieldValue
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FertilizerRequest(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("acre")
    val acre: Double? = 0.0,
    @SerializedName("carat")
    val carat: Double? = 0.0,
    @SerializedName("cropType")
    val cropType: String,
    @SerializedName("fertilizerType")
    val fertilizerType: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("date")
    var date: FieldValue = FieldValue.serverTimestamp(),
    @SerializedName("isRead")
    var isRead: Boolean = false
)