package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase
import java.text.SimpleDateFormat
import java.util.*

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private lateinit var question: Question
    private lateinit var gameSettings: GameSettings
    private lateinit var gameResult: GameResult
    private lateinit var timer: CountDownTimer

    private val _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question>
        get() = _questionLD

    private val _timeUntilFinished = MutableLiveData<String>()
    val timeUntilFinished: LiveData<String>
        get() = _timeUntilFinished

    private val _gameResultLD = MutableLiveData<GameResult>()
    val gameResultLD: LiveData<GameResult>
        get() = _gameResultLD

    private val _gameOver = MutableLiveData<Boolean>()
    val gameOver: LiveData<Boolean>
        get() = _gameOver


    fun getGameSettings(level: Level) {
        gameSettings = getGameSettingsUseCase(level)
        startTimer()
        gameResult = GameResult(false, 0, 0, gameSettings)
        _gameResultLD.value = gameResult
        generateQuestion()
    }

    fun generateQuestion() {
        question = generateQuestionUseCase(gameSettings.maxSumValue)
        _questionLD.value = question
    }

    fun chooseAnswer(number: Int) {
        if (number == question.rightAnswer) {
            ++gameResult.countOfRightAnswers
        }
        gameResult.countOfQuestions++
        if (gameResult.countOfRightAnswers == gameSettings.minCountOfRightAnswers) {
            gameResult.winner = true
            _gameOver.value = true
        }
        _gameResultLD.value = gameResult
        generateQuestion()

    }

    private fun startTimer() {
        timer =
            object : CountDownTimer((gameSettings.gameTimeInSecond * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val time = SimpleDateFormat("mm:ss").format(Date(millisUntilFinished))
                    _timeUntilFinished.value = time
                }
                override fun onFinish() {
                    _gameOver.value = true
                }
            }
        timer.start()
    }


}