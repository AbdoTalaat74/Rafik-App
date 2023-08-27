package com.fertilizers.rafik.domian.entity

import com.google.firebase.firestore.FieldValue
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingRequest(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("productType")
    val productType: String,
    @SerializedName("trainingPlace")
    val trainingPlace: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("date")
    var date: FieldValue = FieldValue.serverTimestamp(),
    @SerializedName("isRead")
    var isRead: Boolean = false
)
