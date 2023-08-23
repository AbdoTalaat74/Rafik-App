package com.example.rafik.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.FertilizerRequest
import com.example.rafik.domian.entity.TrainingRequest
import com.example.rafik.domian.entity.User
import com.example.rafik.domian.repo.FirebaseRepo
import com.example.rafik.utils.Constants.CITIES
import com.example.rafik.utils.Constants.FERTILIZER_REQUEST
import com.example.rafik.utils.Constants.TRAINING_REQUEST
import com.example.rafik.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FireBaseRepoImpl : FirebaseRepo {
    private val tag = "FireBaseRepoImpl"
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    val users = MutableLiveData<List<User>>()
    val cities = MutableLiveData<List<City>>()
    val areas = MutableLiveData<List<String>>()
    var user = MutableLiveData<User>()

    override suspend fun refreshData() {
        TODO("Not yet implemented")
    }

    override suspend fun setUser(user: User): Boolean {
        val res = false
        auth.uid?.let { user.uid = it }
        db.collection(USERS).document(user.uid)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun updateUser(user: User): Boolean {
        val res = false
        db.collection(USERS).document(user.uid)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun getUser() {
        auth.uid?.let {
            db.collection(USERS).document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        document.toObject(User::class.java)?.let { user ->
                            this.user.value = user
                        }
                        Log.d(tag, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(tag, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(tag, "get failed with ", exception)
                }
        }
    }

    override suspend fun getUsers(): List<User> {
        val usersArray = ArrayList<User>()
        db.collection(USERS)
            .get()
            .addOnSuccessListener { result ->
                usersArray.clear()
                for (document in result) {
                    val user: User = document.toObject(User::class.java)
                    usersArray.add(user)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                users.value = usersArray

            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return usersArray
    }

    override suspend fun getCities(): List<City> {
        val citiesArray = ArrayList<City>()
        db.collection(CITIES)
            .get()
            .addOnSuccessListener { result ->
                citiesArray.clear()
                for (document in result) {
                    val city: City = document.toObject(City::class.java)
                    citiesArray.add(city)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                cities.value = citiesArray

            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return citiesArray
    }

    override suspend fun setFertilizerRequest(fertilizerRequest: FertilizerRequest): Boolean {
        var res = false
        val newCityRef = db.collection(FERTILIZER_REQUEST).document()
        fertilizerRequest.id = newCityRef.id
        newCityRef.set(fertilizerRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun setTrainingRequest(trainingRequest: TrainingRequest): Boolean {
        var res = false
        val newCityRef = db.collection(TRAINING_REQUEST).document()
        trainingRequest.id = newCityRef.id
        newCityRef.set(trainingRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }
}