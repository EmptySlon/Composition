package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    var winner: Boolean,
    var countOfRightAnswers: Int,
    var countOfQuestions: Int,
    val gameSettings: GameSettings

): Parcelable
