package ru.kotlincourse.crossfadeproject.utils

import android.media.MediaPlayer
import android.widget.RadioButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

//Блокировка кнопок
fun lockUI(seekBar: DiscreteSeekBar, radioButtonOne: RadioButton, radioButtonTwo: RadioButton)
{
    seekBar.isEnabled = false
    radioButtonOne.isEnabled = false
    radioButtonTwo.isEnabled = false
}

//Разблокировка кнопок
fun unlockUI(seekBar: DiscreteSeekBar, radioButtonOne: RadioButton, radioButtonTwo: RadioButton)
{
    seekBar.isEnabled = true
    radioButtonOne.isEnabled = true
    radioButtonTwo.isEnabled = true
}


//Реализация кроссфейда для первой песни
suspend fun playTrackOne(mediaPlayer: MediaPlayer,mediaPlayerTwo: MediaPlayer, seekBar: DiscreteSeekBar)
{
    var volumeLeftTrackOne : Float = 0.0F
    var volumeRightTrackOne : Float = 0.0F
    var trackDuration = mediaPlayer.duration
    var seekDuration = seekBar.progress * 1000
    mediaPlayer.start()
    for(i in 100 downTo 0 step 1)
    {
        volumeLeftTrackOne+=0.01F
        volumeRightTrackOne+=0.01F
        mediaPlayer.setVolume(volumeLeftTrackOne, volumeRightTrackOne)
        delay(seekDuration.toLong()/100)
    }
    delay(trackDuration.toLong() - seekDuration.toLong() - seekDuration.toLong())
    GlobalScope.launch { playTrackTwo(mediaPlayerTwo, seekBar) }
    for(i in 100 downTo 0 step 1)
    {
        volumeLeftTrackOne-=0.01F
        volumeRightTrackOne-=0.01F
        mediaPlayer.setVolume(volumeLeftTrackOne, volumeRightTrackOne)
        delay(seekDuration.toLong()/100)
    }
    delay(mediaPlayerTwo.duration.toLong()-seekDuration-seekDuration)
}

//Реализация кроссфейда для второй песни
suspend fun playTrackTwo(mediaPlayerTwo: MediaPlayer, seekBar: DiscreteSeekBar)
{
    var volumeLeftTrackTwo : Float = 0.0F
    var volumeRightTrackTwo : Float = 0.0F
    var trackDuration = mediaPlayerTwo.duration
    var seekDuration = seekBar.progress * 1000
    mediaPlayerTwo.setVolume(volumeLeftTrackTwo, volumeRightTrackTwo)
    mediaPlayerTwo.start()
    for(i in 100 downTo 0 step 1)
    {
        volumeLeftTrackTwo+=0.01F
        volumeRightTrackTwo+=0.01F
        mediaPlayerTwo.setVolume(volumeLeftTrackTwo, volumeRightTrackTwo)
        delay(seekDuration.toLong()/100)
    }
    delay(trackDuration.toLong() - seekDuration.toLong() - seekDuration.toLong())
    for(i in 100 downTo 0 step 1)
    {
        volumeLeftTrackTwo-=0.01F
        volumeRightTrackTwo-=0.01F
        mediaPlayerTwo.setVolume(volumeLeftTrackTwo, volumeRightTrackTwo)
        delay(seekDuration.toLong()/100)
    }
}