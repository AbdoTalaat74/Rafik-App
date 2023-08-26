package com.example.rafik.ui.sell_product

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.R
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.SellProductRequest
import kotlinx.coroutines.launch

@SuppressLint("UseCompatLoadingForDrawables")
class SellProductViewModel(private val application: Application) : ViewModel() {

    private val fireBaseRepoImpl = FireBaseRepoImpl()

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: MutableLiveData<Uri?>
        get() = _imageUri

    private val _imageProgressBar = MutableLiveData<Boolean>()
    val imageProgressBar: LiveData<Boolean>
        get() = _imageProgressBar

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage: LiveData<String>
        get() = _dialogMessage

    private val _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp


    private val _sendRequest = MutableLiveData<Boolean>()
    val sendRequest: LiveData<Boolean>
        get() = _sendRequest

    fun onNavigateUp() {
        _navigateUp.postValue(false)
    }

    val productType = ObservableField<String>()
    val amount = ObservableField<String>()
    val address = ObservableField<String>()
    val price = ObservableField<String>()
    val productionDate = ObservableField<String>()

    private var validProductType: Boolean = true
    private var validAmount: Boolean = true
    private var validAddress: Boolean = true
    private var validPrice: Boolean = true
    private var validProductionDate: Boolean = true

    private val _target = MutableLiveData<String>()
    fun postImageUri(imageUri: Uri?) {
        _imageUri.postValue(imageUri)
    }

    fun postTarget(target: String) {
        _target.postValue(target)
    }

    fun postImageProgressBarState(state: Boolean) {
        _imageProgressBar.postValue(state)
    }


    private fun validateForm() {
        Log.e("SellProductViewModel", "validateForm called")

        var message = ""

        if (productType.get().isNullOrBlank()) {
            validProductType = false
            message += application.resources.getString(R.string.please_fill_product_type_field)
        } else {
            validProductType = true
        }
        if (amount.get().isNullOrBlank()) {
            validAmount = false
            message += application.resources.getString(R.string.please_fill_amount_field)
        } else {
            validAmount = true
        }
        if (address.get().isNullOrBlank()) {
            validAddress = false
            message += application.resources.getString(R.string.please_fill_address_field)
        } else {
            validAddress = true

        }
        if (price.get().isNullOrBlank()) {
            validPrice = false
            message += application.resources.getString(R.string.please_fill_price_field)
        } else {
            validPrice = true
        }
        if (productionDate.get().isNullOrBlank()) {
            validProductionDate = false
            message += application.resources.getString(R.string.please_fill_production_date_field)
        } else {
            validProductionDate = true
        }
        _dialogMessage.postValue(message)
    }

    fun validateAndSendRequest() {
        Log.e("SellProductViewModel", "validateAndSendRequest called")
        validateForm()
        if (validProductType && validAmount && validAddress && validPrice && validProductionDate) {
            Log.e("SellProductViewModel", "validation done")
            _sendRequest.postValue(true)
        }
    }

    @Suppress("DEPRECATION")
    fun sendRequest() {
        var bitmap: Bitmap? =null
        Log.i("SellProductViewModel", "sendRequestCalled")
        _imageUri.value?.let {
            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(application.applicationContext.contentResolver, it
                ))
            } else {
                MediaStore.Images.Media.getBitmap(application.applicationContext.contentResolver, it)
            }
        }
        if (user.value != null) {
            val sellProductRequest = SellProductRequest(
                productImage = bitmap,
                productType = productType.get()!!,
                amount = amount.get()!!,
                address = address.get()!!,
                price = price.get()!!,
                productionDate = productionDate.get()!!,
                target = _target.value!!,
                user = user.value!!
            )
            Log.i("SellProductViewModel", sellProductRequest.toString())
            viewModelScope.launch {
               val state = fireBaseRepoImpl.setSellProductRequest(sellProductRequest)
            }
            _navigateUp.postValue(true)
            _sendRequest.postValue(false)
        }
    }

    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getUser()
        }
    }

    val user = fireBaseRepoImpl.user
}