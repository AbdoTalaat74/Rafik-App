package com.example.rafik.ui.organic_fertilizer

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rafik.R
import com.example.rafik.domian.entity.FertilizerRequest

class OrganicFertilizerViewModel(private val application: Application) : ViewModel() {


    val arce = ObservableField<String>()
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
        if (arce.get().isNullOrBlank() && carat.get().isNullOrBlank()) {
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
            val fertilizerRequest =  FertilizerRequest(arce.get()?.toDouble(),carat.get()?.toDouble(),cropType.get()!!,fertilizerType.get()!!)
        }
    }

}