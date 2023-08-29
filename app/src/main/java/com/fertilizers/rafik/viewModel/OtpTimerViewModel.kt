package com.fertilizers.rafik.viewModel

import android.os.CountDownTimer
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OtpTimerViewModel:ViewModel() {
    private val initialTimeInMillis: Long = 60000 // 60 seconds in milliseconds


    private var timer: CountDownTimer? = null
    val isTimerRunning = MutableLiveData(false)
    val timerText = ObservableField<String>()
    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled


    fun setButtonState(state: Boolean) {
        _isButtonEnabled.postValue(state)
    }

    fun stopTimer() {
        timer?.cancel()
        isTimerRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }


    fun startTimer(durationMillis: Long) {
        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the LiveData with the remaining time or other info
                val secondsRemaining = millisUntilFinished / 1000
                timerText.set(secondsRemaining.toString())
            }

            override fun onFinish() {
                timerText.set("0")
                isTimerRunning.value = false
            }
        }
        timer?.start()
        isTimerRunning.value = true
    }


    init {
        startTimer(initialTimeInMillis)
    }

}