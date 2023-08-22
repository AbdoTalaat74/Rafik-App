package com.example.rafik.domian.entity

data class TrainingRequest(
    var id:String = "",
    val productType:String,
    val trainingPlace:String,
    val user:User
)
