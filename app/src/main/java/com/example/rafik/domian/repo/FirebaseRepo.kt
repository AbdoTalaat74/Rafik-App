package com.example.rafik.domian.repo

import com.example.rafik.domian.entity.User

interface FirebaseRepo {
    suspend fun refreshData()
    suspend fun getStatus()

    suspend fun setUser(user: User): Boolean
    suspend fun getUser()
    suspend fun getUsers(): List<User>


}