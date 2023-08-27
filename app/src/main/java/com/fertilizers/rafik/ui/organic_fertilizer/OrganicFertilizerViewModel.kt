package com.fertilizers.rafik.ui.organic_fertilizer

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fertilizers.rafik.R
import com.fertilizers.rafik.data.repo.FireBaseRepoImpl
import com.fertilizers.rafik.domian.entity.FertilizerRequest
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.utils.Constants
import kotlinx.coroutines.launch

class OrganicFertilizerViewModel(private val application: Application) : ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()


    val acre = ObservableField<String>()
    val carat = ObservableField<String>()
    val cropType = ObservableField<String>()
    private val _fertilizerType = MutableLiveData<String>()
    val fertilizerType: LiveData<String>
        get() = _fertilizerType

    private var validArea: Boolean = true
    private var validCropType: Boolean = true
    private var validFertilizerType: Boolean = true

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage: LiveData<String>
        get() = _dialogMessage

    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private val _sendRequest = MutableLiveData<Boolean>()
    val sendRequest: LiveData<Boolean>
        get() = _sendRequest

    val isSuccessfulRequest: LiveData<Constants.Request?>
        get() = fireBaseRepoImpl.fertilizerRequest

    fun setNavigate(state: Boolean) {
        _navigateUp.postValue(state)
    }

    fun onNavigateUp() {
        _navigateUp.postValue(false)
    }

    private fun validateForm() {
        Log.e("OrganicFertilizerViewModel", "invalidForm Called")
        var message = ""
        if (acre.get().isNullOrBlank() && carat.get().isNullOrBlank()) {
            message += application.resources.getString(R.string.please_fill_the_area_field)
            validArea = false
        } else {
            if (acre.get() == "0" && carat.get() == "0") {
                validArea = false
                message += application.resources.getString(R.string.you_must_enter_a_value_per_carat_or_acre)
            } else {
                validArea = true
            }
        }
        if (cropType.get().isNullOrBlank()) {
            message += application.resources.getString(R.string.please_fill_crop_type_field)
            validCropType = false
        } else {
            validCropType = true
        }
        if (_fertilizerType.value.isNullOrBlank()) {
            message += application.resources.getString(R.string.please_fill_fertilizer_type_field)
            validFertilizerType = false
        } else {
            validFertilizerType = true
        }
        message += "\n"
        Log.e("OrganicFertilizerViewModel", message)
        _dialogMessage.postValue(message)
    }


    fun validateAndSendRequest() {
        Log.e("OrganicFertilizerFragment", "validateAndSendRequest called")
        validateForm()
        if (validArea && validCropType && validFertilizerType) {
            Log.e("OrganicFertilizerFragment", "validation done")
            _sendRequest.postValue(true)
        }
    }


    fun sendRequest() {
        validateForm()
        if (user.value != null) {
            val fertilizerRequest = FertilizerRequest(
                "",
                acre.get()?.toDouble(),
                carat.get()?.toDouble(),
                cropType.get()!!,
                _fertilizerType.value!!,
                user.value!!
            )
            acre.set("")
            carat.set("")
            cropType.set("")
            viewModelScope.launch {
                fireBaseRepoImpl.setFertilizerRequest(fertilizerRequest)
            }
            _sendRequest.postValue(false)
        }
    }

    fun postFertilizer(fertilizerType: String) {
        _fertilizerType.postValue(fertilizerType)
    }

    init {
        acre.set("0")
        carat.set("0")
        viewModelScope.launch {
            fireBaseRepoImpl.getUser()
            fireBaseRepoImpl.getFertilizerType()
        }

    }

    val user = fireBaseRepoImpl.user
    val fertilizerTypes: LiveData<List<FertilizerType>> = fireBaseRepoImpl.fertilizerTypes

}