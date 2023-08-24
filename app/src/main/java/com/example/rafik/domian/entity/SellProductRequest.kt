package com.example.rafik.domian.entity

import android.net.Uri

data class SellProductRequest(
    val id:String = "",
    val productImage:Uri? = Uri.parse(""),
    val productType:String,
    val amount:String,
    val address:String,
    val price:String,
    val productionDate:String,
    val target:String,
    val user:User
)