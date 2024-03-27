package com.example.mlkit.text

import android.os.Bundle
import com.example.mlkit.helper.TextHelperActivity
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier

class SpamDetectionActivity:TextHelperActivity() {
    private val MODEL_PATH="model_spam.tflite"
    private lateinit var classifier: NLClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            classifier= NLClassifier.createFromFile(this,MODEL_PATH)


        }catch (e:Exception){

        }
    }

    override fun runClassification(comment: String) {
        val apiResult=classifier.classify(comment)
        val score=apiResult.get(1).score
        if (score>0.8f){

            txtOutput.text="Detected as spam. \n Spam Score ${score}"
        }else{
            txtOutput.text="Not Detected as spam. \n Spam Score ${score}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }

}