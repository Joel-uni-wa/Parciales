package com.example.mygamecolor.sound

import android.content.Context
import android.media.MediaPlayer
import com.example.mygamecolor.R

class SoundManager(private val context: Context) {

    private var playerCorrecto: MediaPlayer? = null
    private var playerIncorrecto: MediaPlayer? = null
    private var playerGameOver: MediaPlayer? = null

    init {
        playerCorrecto = MediaPlayer.create(context, R.raw.correcto)
        playerIncorrecto = MediaPlayer.create(context, R.raw.incorrecto)
        playerGameOver = MediaPlayer.create(context, R.raw.gameover)
    }

    fun playCorrect() {
        playerCorrecto?.seekTo(0)
        playerCorrecto?.start()
    }

    fun playWrong() {
        playerIncorrecto?.seekTo(0)
        playerIncorrecto?.start()
    }

    fun playGameOver() {
        playerGameOver?.seekTo(0)
        playerGameOver?.start()
    }

    fun release() {
        playerCorrecto?.release()
        playerIncorrecto?.release()
        playerGameOver?.release()
    }
}