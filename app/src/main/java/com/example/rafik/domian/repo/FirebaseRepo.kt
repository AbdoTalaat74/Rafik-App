package com.example.rafik.domian.repo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.CropType
import com.example.rafik.domian.entity.FertilizerRequest
import com.example.rafik.domian.entity.FertilizerType
import com.example.rafik.domian.entity.ProductType
import com.example.rafik.domian.entity.SellProductRequest
import com.example.rafik.domian.entity.TrainingArea
import com.example.rafik.domian.entity.TrainingRequest
import com.example.rafik.domian.entity.User

interface FirebaseRepo {
    suspend fun refreshData()

    suspend fun setUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
    suspend fun getUser()
    suspend fun checkUser(phone: String): Boolean
    suspend fun getUsers(): List<User>

    suspend fun setFertilizerRequest(fertilizerRequest: FertilizerRequest): Boolean
    suspend fun setTrainingRequest(trainingRequest: TrainingRequest): Boolean
    suspend fun setSellProductRequest(sellProductRequest: SellProductRequest): Boolean
    suspend fun uploadImage(bitmap: Bitmap, name: String): String
    suspend fun setProductType(productType: ProductType): Boolean
    suspend fun getProductType(): List<ProductType>


    suspend fun setTrainingArea(trainingArea: TrainingArea): Boolean
    suspend fun getTrainingArea(): List<TrainingArea>

    suspend fun setFertilizerType(fertilizerType: FertilizerType): Boolean
    suspend fun getFertilizerType(): List<FertilizerType>

    suspend fun setCropType(cropType: CropType): Boolean
    suspend fun getCropType(): List<CropType>

}