package com.example.rafik.utils

object Constants {
    const val SELL_PRODUCT_REQUEST = "SellProductRequest"
    const val PHOTOS = "photos//"
    const val FERTILIZER_REQUEST = "FertilizerRequest"
    const val TRAINING_REQUEST = "TrainingRequest"
    const val CITIES = "cities"
    const val USERS = "users"
    const val MY_FORMAT = "yyyy/MM/dd" // mention the format you need


    enum class UserFound {
        FOUND, NOT_FOUND, UNKNOWN
    }

    enum class Request {
        SUCCESS, FAILED
    }
}