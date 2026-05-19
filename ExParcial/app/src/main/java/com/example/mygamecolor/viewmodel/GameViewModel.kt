package com.example.mygamecolor.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygamecolor.model.GameColor
import com.example.mygamecolor.model.GameResult
import com.example.mygamecolor.sound.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class GameState { IDLE, PLAYING, FINISHED }
enum class Feedback { CORRECT, WRONG, NONE }

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val soundManager = SoundManager(application)
    private val sharedPreferences = application.getSharedPreferences("color_game_prefs", Context.MODE_PRIVATE)

    private val _currentColor = MutableStateFlow(GameColor.RED)
    val currentColor: StateFlow<GameColor> = _currentColor

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _timeLeft = MutableStateFlow(30)
    val timeLeft: StateFlow<Int> = _timeLeft

    private val _gameState = MutableStateFlow(GameState.IDLE)
    val gameState: StateFlow<GameState> = _gameState

    private val _feedback = MutableStateFlow(Feedback.NONE)
    val feedback: StateFlow<Feedback> = _feedback

    private val _highScore = MutableStateFlow(sharedPreferences.getInt("high_score", 0))
    val highScore: StateFlow<Int> = _highScore

    private val _sessionHistory = MutableStateFlow<List<GameResult>>(emptyList())
    val sessionHistory: StateFlow<List<GameResult>> = _sessionHistory

    private var timerJob: Job? = null
    private var gameCounter = 0

    fun startGame() {
        timerJob?.cancel()
        _score.value = 0
        _timeLeft.value = 30
        _feedback.value = Feedback.NONE
        _gameState.value = GameState.PLAYING
        randomizeColor()
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1
            }
            endGame()
        }
    }

    private fun endGame() {
        _gameState.value = GameState.FINISHED
        soundManager.playGameOver()
        val currentScore = _score.value
        if (currentScore > _highScore.value) {
            _highScore.value = currentScore
            sharedPreferences.edit().putInt("high_score", currentScore).apply()
        }
        gameCounter++
        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _sessionHistory.value = _sessionHistory.value + GameResult(
            gameNumber = gameCounter,
            score = currentScore,
            date = dateStr
        )
    }

    fun onColorSelected(selected: GameColor) {
        if (_gameState.value != GameState.PLAYING) return
        if (selected == _currentColor.value) {
            _score.value += 1
            _feedback.value = Feedback.CORRECT
            soundManager.playCorrect()
            randomizeColor()
        } else {
            _feedback.value = Feedback.WRONG
            soundManager.playWrong()
        }
        viewModelScope.launch {
            delay(600L)
            _feedback.value = Feedback.NONE
        }
    }

    private fun randomizeColor() {
        _currentColor.value = GameColor.values().random()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}