package com.example.mlkit.image

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mlkit.helper.BoxWithLabel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectionActivity : ImageActivity() {
    private lateinit var faceDetector: FaceDetector


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE).enableTracking()
            .build()
        faceDetector = FaceDetection.getClient(highAccuracyOpts)

    }

    @Override
    override fun runClassification(bitmap: Bitmap) {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val inputImage = InputImage.fromBitmap(outputBitmap, 0)
        faceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                Log.d(TAG, "runClassification: $faces")

                if (faces.isEmpty()) {
                    getOutputTextView().text = "No face detected"
                } else {
                    var boxes = mutableListOf<BoxWithLabel>()
                    for (face in faces) {
                        boxes.add(BoxWithLabel(face.boundingBox, face.trackingId.toString()))
                    }
                    drawDetectionResult(boxes, bitmap)
                }
            }.addOnFailureListener {
                Log.d(TAG, "runClassification: ${it.stackTrace}")
            }
    }
    companion object{
        const val TAG="ALEXA"
    }
}