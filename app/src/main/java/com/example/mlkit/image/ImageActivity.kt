package com.example.mlkit.image

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mlkit.helper.ImageHelperActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

 open class ImageActivity : ImageHelperActivity() {

    private lateinit var imageLabeler: ImageLabeler

    @RequiresApi(Build.VERSION_CODES.S)
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLabeler =
            ImageLabeling.getClient(
                ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
            )

    }

    @Override
    override fun runClassification(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        imageLabeler.process(inputImage)
            .addOnSuccessListener { imageLabels ->
                Log.d(TAG, "runClassification: success")
                if (imageLabels.isNotEmpty()) {
                    var builder = ""
                    for (label in imageLabels) {
                        Log.d(TAG, "runClassification: ${label.text}")
                        builder += "${label.text}:${label.confidence}\n"
                    }
                    Log.d(TAG, "runClassification: $builder")
                    getOutputTextView().text = builder
                } else {
                    getOutputTextView().text = "Unable to classify"
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "runClassification: Failed to process image", e)
                // Handle failure, e.g., show an error message
            }
    }
}