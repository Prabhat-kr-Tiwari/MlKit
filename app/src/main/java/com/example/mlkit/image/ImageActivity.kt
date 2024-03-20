package com.example.mlkit.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mlkit.helper.BoxWithLabel
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
     fun drawDetectionResult(boxes: List<BoxWithLabel>,bitmap: Bitmap) {
         val outputBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true)
         val canvas=Canvas(outputBitmap)
         val penRect=Paint()
         penRect.color = Color.RED
         penRect.style=Paint.Style.STROKE
         penRect.strokeWidth=8f


         val penLabel=Paint()
         penLabel.color = Color.YELLOW
         penLabel.style=Paint.Style.FILL_AND_STROKE
         penLabel.textSize=96f

         for (boxWithLabel in boxes){
             canvas.drawRect(boxWithLabel.rect,penRect)

             //Rect
             val labelSize= Rect(0,0,0,0)
             penLabel.getTextBounds(boxWithLabel.label,0,boxWithLabel.label.length,labelSize)


             val fontSize=penLabel.textSize*boxWithLabel.rect.width()/labelSize.width()
             if (fontSize<penLabel.textSize){
                 penLabel.textSize=fontSize
             }
             canvas.drawText(boxWithLabel.label,
                 boxWithLabel.rect.left.toFloat(),boxWithLabel.rect.top.toFloat()+ labelSize.height().toFloat(),penLabel)
         }
         inputImageView().setImageBitmap(outputBitmap)



     }


 }