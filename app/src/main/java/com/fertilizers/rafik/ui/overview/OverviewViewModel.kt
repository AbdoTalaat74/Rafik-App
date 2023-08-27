package com.fertilizers.rafik.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fertilizers.rafik.data.repo.FireBaseRepoImpl
import com.fertilizers.rafik.domian.entity.CropType
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.domian.entity.TrainingArea
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {
    private val fireBaseRepoImpl = FireBaseRepoImpl()

    init {
        viewModelScope.launch {
            fireBaseRepoImpl.getTrainingArea()
            fireBaseRepoImpl.getCropType()
            fireBaseRepoImpl.getFertilizerType()
        }
    }

    val cropType: LiveData<List<CropType>> = fireBaseRepoImpl.cropTypes
    val fertilizerTypes: LiveData<List<FertilizerType>> = fireBaseRepoImpl.fertilizerTypes
    val trainingAreas: LiveData<List<TrainingArea>> = fireBaseRepoImpl.trainingAreas
}