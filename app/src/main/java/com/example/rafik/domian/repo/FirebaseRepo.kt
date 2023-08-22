package com.example.rafik.domian.repo

import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.FertilizerRequest
import com.example.rafik.domian.entity.User

interface FirebaseRepo {
    suspend fun refreshData()

    suspend fun setUser(user: User): Boolean
    suspend fun getUser()
    suspend fun getUsers(): List<User>

    suspend fun getCities(): List<City>

    suspend fun setFertilizerRequest(fertilizerRequest: FertilizerRequest): Boolean

}