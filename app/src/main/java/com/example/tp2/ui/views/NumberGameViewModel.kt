package com.example.tp2.ui.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.*
import com.example.tp2.data.local.entities.MaxScore
import com.example.tp2.data.repositories.ScoreRepository

class NumberGameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScoreRepository = ScoreRepository(application)

    private val _currentScore = mutableIntStateOf(0)
    val currentScore: State<Int> = _currentScore

    private val _maxScore = mutableIntStateOf(0)
    val maxScore: State<Int> = _maxScore

    init {
        viewModelScope.launch {
            repository.getMaxScore().collect { max ->
                _maxScore.intValue = max?.value ?: 0
            }
        }
    }

    fun atemp(success: Boolean) {
        if (success) {
            _currentScore.intValue += 10
            if (_currentScore.intValue > _maxScore.intValue) {
                updateMaxScore(_currentScore.intValue)
            }
        } else {
            _currentScore.intValue = 0
        }
    }

    private fun updateMaxScore(new: Int) {
        viewModelScope.launch {
            repository.insertMaxScore(MaxScore(value = new))
        }
    }
}