package com.example.mlkit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mlkit.databinding.ActivityMainBinding
import com.example.mlkit.helper.ImageHelperActivity
import com.example.mlkit.image.FaceDetectionActivity
import com.example.mlkit.image.FlowerIdentificationActivity
import com.example.mlkit.image.ImageActivity
import com.example.mlkit.image.ImageClassificationActivity
import com.example.mlkit.image.ObjectDetectionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btn.setOnClickListener {
            startActivity(Intent(this, ImageActivity::class.java))
        }
        binding.btn2.setOnClickListener {
            startActivity(Intent(this, FlowerIdentificationActivity::class.java))
        }
        binding.btn3.setOnClickListener {
            startActivity(Intent(this, ObjectDetectionActivity::class.java))
        }
        binding.btn4.setOnClickListener {
            startActivity(Intent(this, FaceDetectionActivity::class.java))
        }
    }
}