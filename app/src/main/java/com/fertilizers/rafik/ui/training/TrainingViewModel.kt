package com.fertilizers.rafik.ui.training

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fertilizers.rafik.data.repo.FireBaseRepoImpl
import com.fertilizers.rafik.domian.entity.TrainingRequest
import com.fertilizers.rafik.utils.Constants
import kotlinx.coroutines.launch

class TrainingViewModel(application: Application) : ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()
    private val _productType = MutableLiveData<String>()
    private val _trainingPlace = MutableLiveData<String>()
    private lateinit var trainingRequest: TrainingRequest


    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp


    private val _sendRequest = MutableLiveData<Boolean>()
    val sendRequest: LiveData<Boolean>
        get() = _sendRequest

    val isSuccessfulRequest:LiveData<Constants.Request?>
        get() = fireBaseRepoImpl.trainingRequest

    fun setNavigateUp(state: Boolean){
        _navigateUp.postValue(state)
    }

    fun onNavigateUp() {
        _navigateUp.postValue(false)
    }

    fun postProductType(productType: String) {
        _productType.postValue(productType)
    }

    fun postTrainingPlace(trainingPlace: String) {
        _trainingPlace.postValue(trainingPlace)
    }


    fun validateAndSendRequest() {
        Log.e("TrainingViewModel", "validateAndSendRequest called")
        Log.e("TrainingViewModel", _productType.value!!)
        Log.e("TrainingViewModel", _trainingPlace.value!!)
        if (!(_trainingPlace.value.isNullOrBlank() && _productType.value.isNullOrBlank())) {
            Log.e("TrainingViewModel", "validation done")
            _sendRequest.postValue(true)
        }
    }


    fun sendRequest() {
        Log.e("TrainingViewModel", "sendRequest called")
        Log.e("TrainingViewModel", _productType.value!!)
        Log.e("TrainingViewModel", _trainingPlace.value!!)
        if (user.value != null) {
            trainingRequest = TrainingRequest(
                productType = _productType.value!!,
                trainingPlace = _trainingPlace.value!!,
                user = user.value!!
            )
            viewModelScope.launch {
                fireBaseRepoImpl.setTrainingRequest(trainingRequest)
            }
            _sendRequest.postValue(false)
        }

    }


    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getUser()
            fireBaseRepoImpl.getTrainingArea()
            fireBaseRepoImpl.getTrainingArea()
        }
    }

    val user = fireBaseRepoImpl.user
    val trainingAreas = fireBaseRepoImpl.trainingAreas
    val productTypes=fireBaseRepoImpl.productTypes

}

