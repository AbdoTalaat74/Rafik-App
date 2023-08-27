package com.fertilizers.rafik.data.repo

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fertilizers.rafik.domian.entity.CropType
import com.fertilizers.rafik.domian.entity.FertilizerRequest
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.domian.entity.ProductType
import com.fertilizers.rafik.domian.entity.SellProductRequest
import com.fertilizers.rafik.domian.entity.TrainingArea
import com.fertilizers.rafik.domian.entity.TrainingRequest
import com.fertilizers.rafik.domian.entity.User
import com.fertilizers.rafik.domian.repo.FirebaseRepo
import com.fertilizers.rafik.utils.Constants.CROP_TYPE
import com.fertilizers.rafik.utils.Constants.Request
import com.fertilizers.rafik.utils.Constants.UserFound
import com.fertilizers.rafik.utils.Constants.FERTILIZER_REQUEST
import com.fertilizers.rafik.utils.Constants.FERTILIZER_TYPE
import com.fertilizers.rafik.utils.Constants.PHOTOS
import com.fertilizers.rafik.utils.Constants.PRODUCT_TYPE
import com.fertilizers.rafik.utils.Constants.SELL_PRODUCT_REQUEST
import com.fertilizers.rafik.utils.Constants.TRAINING_AREA
import com.fertilizers.rafik.utils.Constants.TRAINING_REQUEST
import com.fertilizers.rafik.utils.Constants.USERS
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
    private val users = MutableLiveData<List<User>>()
    val productTypes = MutableLiveData<List<ProductType>>()
    val trainingAreas = MutableLiveData<List<TrainingArea>>()
    val fertilizerTypes = MutableLiveData<List<FertilizerType>>()
    val cropTypes = MutableLiveData<List<CropType>>()
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

    override suspend fun setProductType(productType: ProductType): Boolean {
        var res = false
        val newRequestRef = db.collection(PRODUCT_TYPE).document()
        productType.uid = newRequestRef.id
        newRequestRef.set(productType)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun getProductType(): List<ProductType> {
        val array = ArrayList<ProductType>()
        db.collection(PRODUCT_TYPE)
            .get()
            .addOnSuccessListener { result ->
                array.clear()
                for (document in result) {
                    val product: ProductType = document.toObject(ProductType::class.java)
                    array.add(product)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                productTypes.value = array
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return array
    }

    override suspend fun setTrainingArea(trainingArea: TrainingArea): Boolean {
        var res = false
        val newRequestRef = db.collection(TRAINING_AREA).document()
        trainingArea.uid = newRequestRef.id
        newRequestRef.set(trainingArea)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun getTrainingArea(): List<TrainingArea> {
        val array = ArrayList<TrainingArea>()
        db.collection(TRAINING_AREA)
            .get()
            .addOnSuccessListener { result ->
                array.clear()
                for (document in result) {
                    val trainingArea: TrainingArea = document.toObject(TrainingArea::class.java)
                    array.add(trainingArea)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                trainingAreas.value = array
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return array
    }

    override suspend fun setFertilizerType(fertilizerType: FertilizerType): Boolean {
        var res = false
        val newRequestRef = db.collection(FERTILIZER_TYPE).document()
        fertilizerType.uid = newRequestRef.id
        newRequestRef.set(fertilizerType)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun getFertilizerType(): List<FertilizerType> {
        val array = ArrayList<FertilizerType>()
        db.collection(FERTILIZER_TYPE)
            .get()
            .addOnSuccessListener { result ->
                array.clear()
                for (document in result) {
                    val fertilizerType: FertilizerType = document.toObject(FertilizerType::class.java)
                    array.add(fertilizerType)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                fertilizerTypes.value = array
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return array
    }

    override suspend fun setCropType(cropType: CropType): Boolean {
        var res = false
        val newRequestRef = db.collection(CROP_TYPE).document()
        cropType.uid = newRequestRef.id
        newRequestRef.set(cropType)
            .addOnSuccessListener { documentReference ->
                Log.d(tag, "DocumentSnapshot added with ID: $documentReference")
                res = true
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding document", e)
            }
        return res
    }

    override suspend fun getCropType(): List<CropType> {
        val array = ArrayList<CropType>()
        db.collection(CROP_TYPE)
            .get()
            .addOnSuccessListener { result ->
                array.clear()
                for (document in result) {
                    val cropType: CropType = document.toObject(CropType::class.java)
                    array.add(cropType)
                    Log.d(tag, "${document.id} => ${document.data}")
                }
                cropTypes.value = array
            }
            .addOnFailureListener { exception ->
                Log.w(tag, "Error getting documents.", exception)
            }
        return array
    }
}