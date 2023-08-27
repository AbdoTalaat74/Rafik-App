package com.example.rafik.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import kotlinx.coroutines.launch

class OverviewViewModel:ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()

    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getTrainingArea()
            fireBaseRepoImpl.getCropType()
            fireBaseRepoImpl.getFertilizerType()
        }
    }
    val cropType=fireBaseRepoImpl.cropTypes
    val fertilizerTypes=fireBaseRepoImpl.fertilizerTypes
    val trainingAreas=fireBaseRepoImpl.trainingAreas
}