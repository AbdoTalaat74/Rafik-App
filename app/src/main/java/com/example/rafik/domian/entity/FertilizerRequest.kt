package com.example.rafik.domian.entity

data class FertilizerRequest(
    var id:String="",
    val acre:Double? = 0.0,
    val carat:Double?=0.0,
    val cropType:String,
    val fertilizerType:String,
    val user:User
)