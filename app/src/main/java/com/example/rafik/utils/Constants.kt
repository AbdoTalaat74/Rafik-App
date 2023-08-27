package com.example.rafik.utils

object Constants {
    const val SELL_PRODUCT_REQUEST = "SellProductRequest"
    const val PRODUCT_TYPE = "ProductType"
    const val TRAINING_AREA = "TrainingArea"
    const val FERTILIZER_TYPE = "FertilizerType"
    const val CROP_TYPE = "CropType"
    const val PHOTOS = "photos//"
    const val FERTILIZER_REQUEST = "FertilizerRequest"
    const val TRAINING_REQUEST = "TrainingRequest"
    const val USERS = "users"
    const val MY_FORMAT = "yyyy/MM/dd" // mention the format you need

    enum class UserFound {
        FOUND, NOT_FOUND
    }

    enum class Request {
        SUCCESS, FAILED
    }
}