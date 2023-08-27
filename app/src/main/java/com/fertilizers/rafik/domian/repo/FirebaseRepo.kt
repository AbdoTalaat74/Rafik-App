package com.fertilizers.rafik.domian.repo

import android.graphics.Bitmap
import com.fertilizers.rafik.domian.entity.CropType
import com.fertilizers.rafik.domian.entity.FertilizerRequest
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.domian.entity.ProductType
import com.fertilizers.rafik.domian.entity.SellProductRequest
import com.fertilizers.rafik.domian.entity.TrainingArea
import com.fertilizers.rafik.domian.entity.TrainingRequest
import com.fertilizers.rafik.domian.entity.User

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