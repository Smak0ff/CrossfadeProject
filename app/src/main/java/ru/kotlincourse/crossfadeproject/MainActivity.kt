package ru.kotlincourse.crossfadeproject

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import ru.kotlincourse.crossfadeproject.utils.lockUI
import ru.kotlincourse.crossfadeproject.utils.playTrackOne
import ru.kotlincourse.crossfadeproject.utils.unlockUI
import java.lang.Exception

class MainActivity : AppCompatActivity(),CompoundButton.OnCheckedChangeListener {
    private lateinit var acdcMediaPlayer: MediaPlayer
    private lateinit var rhcpMediaPlayer: MediaPlayer
    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggleButtonId.setOnCheckedChangeListener(this)
    }

    //Настраиваем слушателя для кнопки
    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        //Обернул всё в два try/catch, т.к. в условиях написано, что необходима обработка исключений
        try {
            if (isChecked) {
                //Вся логика помещена в functions.kt
                lockUI(seekBarId, radioButtonAcdcId, radioButtonRhcpId)
                init()
                job = GlobalScope.launch {
                    //Обернул корутину в отдельный try/catch, чтобы была возможность вызвать Toast на экспешн и отменить корутину
                    try {
                        when (radioGroupId.checkedRadioButtonId) {
                            R.id.radioButtonAcdcId -> {
                                while (true) {

                                    playTrackOne(acdcMediaPlayer, rhcpMediaPlayer, seekBarId)
                                }
                            }

                            R.id.radioButtonRhcpId -> {
                                while (true) {
                                    playTrackOne(rhcpMediaPlayer, acdcMediaPlayer, seekBarId)
                                }
                            }
                        }
                    }
                    catch (exception : Exception)
                    {
                        withContext(Dispatchers.Main)
                        {
                            cancelAllProcess()
                            toggleButtonId.isChecked=false
                            Toast.makeText(this@MainActivity,"Произошла ошибка, свяжитесь, пожалуйста, с создателем программы. Ошибка $exception",Toast.LENGTH_LONG).show() }
                    }
                }
            } else {
                cancelAllProcess()
            }
        }
        catch (exception : Exception)
        {
            exceptionAction(exception)
        }

    }

    //Инициализация песен
    private fun init() {
        acdcMediaPlayer = MediaPlayer.create(this, R.raw.acdc)
        rhcpMediaPlayer = MediaPlayer.create(this, R.raw.rhcp)
    }
    //Отмена корутины, разблокировка UI и выключение песен
    private fun cancelAllProcess()
    {
        unlockUI(seekBarId, radioButtonAcdcId, radioButtonRhcpId)
        job.cancel()
        acdcMediaPlayer.stop()
        rhcpMediaPlayer.stop()
    }
    //Метод для обработки исключений
    private fun exceptionAction(exception : Exception)
    {
        toggleButtonId.isChecked=false
        Toast.makeText(this@MainActivity,"Произошла ошибка, свяжитесь, пожалуйста, с создателем программы. Ошибка $exception",Toast.LENGTH_LONG).show()
    }

}