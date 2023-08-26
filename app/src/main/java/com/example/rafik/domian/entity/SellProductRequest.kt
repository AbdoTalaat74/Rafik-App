package com.example.rafik.domian.entity

import android.graphics.Bitmap
import com.google.firebase.firestore.FieldValue
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SellProductRequest(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("productImage")
    var productImage: Bitmap? = null,
    @SerializedName("ImageUrl")
    var imageUrl: String? = null,
    @SerializedName("productType")
    val productType: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("productionDate")
    val productionDate: String,
    @SerializedName("target")
    val target: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("date")
    var date: FieldValue = FieldValue.serverTimestamp(),
    @SerializedName("isRead")
    var isRead: Boolean = false
)