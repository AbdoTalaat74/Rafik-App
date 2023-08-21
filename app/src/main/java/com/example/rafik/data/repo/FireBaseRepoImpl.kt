package com.example.rafik.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.User
import com.example.rafik.domian.repo.FirebaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FireBaseRepoImpl : FirebaseRepo {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    val users = MutableLiveData<List<User>>()
    val cities = MutableLiveData<List<City>>()
    val areas = MutableLiveData<List<String>>()
    var user = MutableLiveData<User>()


    private val TAG = "FireBaseRepoImpl"
    override suspend fun refreshData() {
        TODO("Not yet implemented")
    }

    override suspend fun setUser(user: User): Boolean {
        val res = false
        auth.uid?.let { user.uid = it }
        db.collection("users").document(user.uid)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return res
    }

    override suspend fun getUser() {
        auth.uid?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        document.toObject(User::class.java)?.let { user ->
                            this.user.value = user
                        }
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    override suspend fun getUsers(): List<User> {
        val usersArray = ArrayList<User>()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                usersArray.clear()
                for (document in result) {
                    val user: User = document.toObject(User::class.java)
                    usersArray.add(user)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                users.value = usersArray

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return usersArray
    }

    override suspend fun getCities(): List<City> {
        val citiesArray = ArrayList<City>()
        db.collection("cities")
            .get()
            .addOnSuccessListener { result ->
                citiesArray.clear()
                for (document in result) {
                    val city: City = document.toObject(City::class.java)
                    citiesArray.add(city)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                cities.value = citiesArray

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return citiesArray
    }
}