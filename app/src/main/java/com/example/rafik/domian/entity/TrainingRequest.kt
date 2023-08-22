package com.example.rafik.domian.entity

data class TrainingRequest(
    val id:String = "",
    val productType:String,
    val trainingPlace:String,
    val user:User
)
