package com.example.mlkit.helper

import android.graphics.Rect

data class BoxWithLabel(
    val rect: Rect,
    val label: String
)