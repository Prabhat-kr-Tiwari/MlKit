package com.example.mlkit.image

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mlkit.helper.BoxWithLabel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectionActivity : ImageActivity() {

    private lateinit var objectDetector: ObjectDetector

    @RequiresApi(Build.VERSION_CODES.S)
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val objectDetectorOptions =
            ObjectDetectorOptions.Builder().setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
        objectDetector = ObjectDetection.getClient(objectDetectorOptions)

    }

    @Override
    override fun runClassification(bitmap: Bitmap) {
        val inputImage= InputImage.fromBitmap(bitmap,0)
        objectDetector.process(inputImage)
            .addOnSuccessListener { detectObjects->

                if (detectObjects.isNotEmpty()){
                    var builder=""
                    var boxWithLabels= mutableListOf<BoxWithLabel>()
                    for (objects in detectObjects){
                        if (objects.labels.isNotEmpty()){
                            val label= objects.labels[0].text
                            builder+="${label}\n  : "
                            builder+="${objects.labels[0].confidence}\n"
                            boxWithLabels.add(BoxWithLabel(objects.boundingBox,label))

                        }else{
                            builder+="${"Unknown"}\n"
                        }
                    }
                    getOutputTextView().text=builder.toString()
                    drawDetectionResult(boxWithLabels,bitmap)
                }else{
                    getOutputTextView().text="Object not detected"
                }

            }.addOnFailureListener {e->
                Log.d(TAG, "runClassification: $e")


            }
    }
}