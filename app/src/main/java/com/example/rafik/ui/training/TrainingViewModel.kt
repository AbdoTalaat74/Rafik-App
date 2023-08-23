package com.example.rafik.ui.training

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.TrainingRequest
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.launch

class TrainingViewModel(application: Application) : ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()
    private val _productType = MutableLiveData<String>()
    private val _trainingPlace = MutableLiveData<String>()
    private lateinit var trainingRequest: TrainingRequest


    private val _onNavigateUp = MutableLiveData<Boolean>()
    val onNavigateUp: LiveData<Boolean>
        get() = _onNavigateUp

    fun postProductType(productType: String) {
        _productType.postValue(productType)
    }

    fun postTrainingPlace(trainingPlace: String) {
        _trainingPlace.postValue(trainingPlace)
    }

    fun sendRequest() {
        Log.e("TrainingViewModel", "sendRequest called")
        Log.e("TrainingViewModel", _productType.value!!)
        Log.e("TrainingViewModel", _trainingPlace.value!!)
        if (_trainingPlace.value.isNullOrBlank() && _productType.value.isNullOrBlank()) {
            Log.e("TrainingViewModel", "Null values")
            return
        } else {
            if (user.value != null) {
                trainingRequest = TrainingRequest(
                    productType = _productType.value!!,
                    trainingPlace = _trainingPlace.value!!,
                    user = user.value!!
                )
                viewModelScope.launch {
                    fireBaseRepoImpl.setTrainingRequest(trainingRequest)
                }

                _onNavigateUp.value = true
            }
        }
    }


    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getUser()
        }
    }

    val user = fireBaseRepoImpl.user
}

