package com.example.rafik.data.repo

import com.example.rafik.domian.entity.User
import com.example.rafik.domian.repo.FirebaseRepo

class FireBaseRepoImpl :FirebaseRepo {
    override suspend fun refreshData() {
        TODO("Not yet implemented")
    }

    override suspend fun getStatus() {
        TODO("Not yet implemented")
    }

    override suspend fun setUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }
}