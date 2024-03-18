package com.example.mlkit.image

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.mlkit.helper.ImageHelperActivity

class ImageClassificationActivity:ImageHelperActivity() {


    @RequiresApi(Build.VERSION_CODES.S)
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Override
    override fun runClassification(bitmap: Bitmap) {
        super.runClassification(bitmap)
    }
}