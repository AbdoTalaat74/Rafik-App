package com.example.rafik.ui.organic_fertilizer

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.R
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.FertilizerRequest
import kotlinx.coroutines.launch

class OrganicFertilizerViewModel(private val application: Application) : ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()


    val acre = ObservableField<String>()
    val carat = ObservableField<String>()
    val cropType = ObservableField<String>()
    val fertilizerType = ObservableField<String>()

    private var validArea: Boolean = true
    private var validCropType: Boolean = true
    private var validFertilizerType: Boolean = true

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage: LiveData<String>
        get() = _dialogMessage
//    fun sendOrganicFertilizerRequest(){
//        invalidForm()
//    }


    private fun validateForm() {
        Log.e("OrganicFertilizerViewModel", "invalidForm Called")
        var message = ""
        if (acre.get().isNullOrBlank() && carat.get().isNullOrBlank()) {
            message += application.resources.getString(R.string.pleasefill_the_area_field)
            validArea = false
        } else {
            validArea = true
        }
        if (cropType.get().isNullOrBlank()) {
            message += application.resources.getString(R.string.please_fill_crop_type_field)
            validCropType = false
        } else {
            validCropType = true
        }
        if (fertilizerType.get().isNullOrBlank()) {
            message += application.resources.getString(R.string.please_fill_fertilizer_type_field)
            validFertilizerType = false
        } else {
            validFertilizerType = true
        }
        message += "\n"
        Log.e("OrganicFertilizerViewModel", message)
        _dialogMessage.postValue(message)
    }

    fun sendRequest() {
        validateForm()
        if (validArea && validCropType && validFertilizerType) {
//            Log.e("OrganicFertilizerViewModel", "Request Sent")
            val fertilizerRequest =  FertilizerRequest(acre.get()?.toDouble(),carat.get()?.toDouble(),cropType.get()!!,fertilizerType.get()!!, user.value!!)
        //    fireBaseRepoImpl.
        }
    }
    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getUser()
        }
    }
    val user = fireBaseRepoImpl.user
}