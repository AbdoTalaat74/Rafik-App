package com.example.rafik.domian.entity

data class FertilizerRequest(
    val arce:Double? = 0.0,
    val carat:Double?=0.0,
    val cropType:String,
    val fertilizerType:String
)