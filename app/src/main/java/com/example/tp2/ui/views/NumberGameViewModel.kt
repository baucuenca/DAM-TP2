package com.example.tp2.ui.views

import com.example.tp2.data.local.entities.MaxScore
import com.example.tp2.data.local.dao.ScoreDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class NumberGameViewModel(private val dao: ScoreDao) : ViewModel() {
    private val _currentScore = mutableIntStateOf(0)
    val currentScore: State<Int> = _currentScore

    private val _maxScore = mutableIntStateOf(0)
    val maxScore: State<Int> = _maxScore

    init {
        viewModelScope.launch {
            dao.getMaxScore().collect { max ->
                _maxScore.intValue = max?.value ?: 0
            }
        }
    }

    fun atemp(success: Boolean) {
        if (success) {
            _currentScore.intValue++
            if (_currentScore.intValue > _maxScore.intValue) {
                updateMaxScore(_currentScore.intValue)
            }
        } else {
            _currentScore.intValue = 0
        }
    }

    private fun updateMaxScore(new: Int) {
        viewModelScope.launch {
            dao.insertMaxScore(MaxScore(value = new))
        }
    }
}
