package com.example.rafik.data.repo

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rafik.domian.entity.FertilizerRequest
import com.example.rafik.domian.entity.SellProductRequest
import com.example.rafik.domian.entity.TrainingRequest
import com.example.rafik.domian.entity.User
import com.example.rafik.domian.repo.FirebaseRepo
import com.example.rafik.utils.Constants.Request
import com.example.rafik.utils.Constants.UserFound
import com.example.rafik.utils.Constants.FERTILIZER_REQUEST
import com.example.rafik.utils.Constants.PHOTOS
import com.example.rafik.utils.Constants.SELL_PRODUCT_REQUEST
import com.example.rafik.utils.Constants.TRAINING_REQUEST
import com.example.rafik.utils.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class FireBaseRepoImpl : FirebaseRepo {
    private val tag = "FireBaseRepoImpl"
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage
    val users = MutableLiveData<List<User>>()
    var user = MutableLiveData<User>()
    var isUserFound = MutableLiveData<UserFound?>()
    var fertilizerRequest = MutableLiveData<Request?>()
    var sellProductRequest = MutableLiveData<Request?>()
    var trainingRequest = MutableLiveData<Request?>()

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

    override suspend fun checkUser(phone: String): Boolean {
        var res = false
        db.collection(USERS).whereEqualTo("phone", phone).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.exists()) {
                        Log.d(tag, "User already exists.")
                        res = true
                        isUserFound.value = UserFound.FOUND
                    } else {
                        Log.d(tag, "User doesn't exist.")
                        isUserFound.value = UserFound.NOT_FOUND
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
                isUserFound.value = UserFound.NOT_FOUND
            }.addOnCompleteListener {
                Log.d(tag, "CompleteListener")
                if (isUserFound.value != UserFound.FOUND) {
                    isUserFound.value = UserFound.NOT_FOUND
                }
            }
        return res
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

    override suspend fun setFertilizerRequest(fertilizerRequest: FertilizerRequest): Boolean {
        var res = false
        val newRequestRef = db.collection(FERTILIZER_REQUEST).document()
        fertilizerRequest.id = newRequestRef.id
        newRequestRef.set(fertilizerRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                this.fertilizerRequest.value = Request.SUCCESS
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
                this.fertilizerRequest.value = Request.FAILED

            }
        return res
    }

    override suspend fun setTrainingRequest(trainingRequest: TrainingRequest): Boolean {
        var res = false
        val newRequestRef = db.collection(TRAINING_REQUEST).document()
        trainingRequest.id = newRequestRef.id
        newRequestRef.set(trainingRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                this.trainingRequest.value = Request.SUCCESS
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
                this.trainingRequest.value = Request.FAILED
            }
        return res
    }

    override suspend fun setSellProductRequest(sellProductRequest: SellProductRequest): Boolean {
        var res = false
        val newRequestRef = db.collection(SELL_PRODUCT_REQUEST).document()
        sellProductRequest.id = newRequestRef.id
        sellProductRequest.productImage?.let {
            sellProductRequest.imageUrl = uploadImage(it, newRequestRef.id)
        }
        sellProductRequest.productImage = null
        newRequestRef.set(sellProductRequest)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                this.sellProductRequest.value = Request.SUCCESS

                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
                this.sellProductRequest.value = Request.FAILED
            }
        return res
    }

    override suspend fun uploadImage(bitmap: Bitmap, name: String): String {
        val url = "$PHOTOS + ${user.value!!.uid}//$name"
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask =
            storage.reference.child("$PHOTOS + ${user.value!!.uid}//$name").putBytes(data)
        uploadTask.addOnFailureListener { e ->
            Log.w(tag, "Error adding document", e)
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(tag, "taskSnapshot added with ID: ${taskSnapshot.storage.downloadUrl}")
        }
        return url
    }
}