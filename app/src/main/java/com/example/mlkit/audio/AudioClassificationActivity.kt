package com.example.mlkit.audio

import android.media.AudioRecord
import android.os.Bundle
import android.util.Log
import com.example.mlkit.helper.AudioHelperActivity
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.Timer
import java.util.TimerTask

class AudioClassificationActivity : AudioHelperActivity() {
    var model = "yamnet_classification.tflite"
    private lateinit var audioRecord: AudioRecord
    private lateinit var timerTask: TimerTask
    private lateinit var audioClassifier: AudioClassifier
    private lateinit var tensorAudio: TensorAudio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            audioClassifier = AudioClassifier.createFromFile(this, model)
        } catch (e: Exception) {

            Log.d(TAG, "onCreate: ")
        }

        //CREATE A TENSOR AUDIO
        tensorAudio = audioClassifier.createInputTensorAudio()

        val audioHelperActivity = AudioHelperActivity()
        startRecordingButton.setOnClickListener {

            val format=audioClassifier.requiredTensorAudioFormat
            val specs="Number of channels:  "+format.channels+"\n"+"Sample rate:  "+format.sampleRate
            specsTextView.text=specs
            audioRecord=audioClassifier.createAudioRecord()
            audioRecord.startRecording()
             timerTask = object : TimerTask() {
                override fun run() {
                    val output=audioClassifier.classify(tensorAudio)
                    val finalOutput= mutableListOf<Category>()
                    for (classification in output){
                        for (category in classification.categories){
                            if (category.score>0.3f){

                                finalOutput.add(category)
                            }
                        }
                    }

                    var outputStr=""
                    for (category in finalOutput){
                        outputStr+= category.label +":"+"${category.score}\n"
                    }
                    runOnUiThread(object :Runnable{
                        override fun run() {
                            outputTextView.text=outputStr
                        }

                    })
                }
            }
            val timer=Timer()
            timer.scheduleAtFixedRate(timerTask,1,500)



        }
        stopRecordingButton.setOnClickListener {

            audioRecord.stop()
            timerTask.cancel()
        }

    }

    companion object {
        const val TAG = "ALEXAJI"
    }


}