package com.fertilizers.rafik.ui.sell_product

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SellProductVMFactory constructor(private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SellProductViewModel::class.java)) {
                SellProductViewModel(this.application) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

