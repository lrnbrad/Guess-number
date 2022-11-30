package com.example.guessnumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class GameStatus {
    Won, Lose, InputError
}

class GuessNumberViewModel : ViewModel() {
    private val _answer = MutableLiveData(0)
    private val _guessesNumber = MutableLiveData("")
    private val _showDialog = MutableLiveData(false)
    private val _gameStatus = MutableLiveData(GameStatus.InputError)

    val answer: LiveData<Int> = _answer
    val guessesNumber: LiveData<String> = _guessesNumber
    val showDialog: LiveData<Boolean> = _showDialog
    val gameStatus: LiveData<GameStatus> = _gameStatus

    fun onGuessesNumberChanged(newGuesses: String) {
        _guessesNumber.value = newGuesses
    }

    fun onShowDialogChange() {
        _showDialog.value = !_showDialog.value!!
    }

    fun onGameStatusChange(newStatus: GameStatus) {
        _gameStatus.value = newStatus
    }

    fun checkAnswer(): Boolean {
        return answer.value == (guessesNumber.value!!.toInt())
    }

    fun changeAnswer() {
        _answer.value = Random.nextInt(0, 10000)
    }

    init {
        changeAnswer()
    }
}