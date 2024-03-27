package com.example.mlkit.helper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mlkit.R
import com.example.mlkit.databinding.ActivityTextHelperBinding
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import org.w3c.dom.Text

open class TextHelperActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTextHelperBinding
    private lateinit var editTextComment: EditText
    private lateinit var btnPostComment:Button
     lateinit var txtOutput:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTextHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWidgets()

        btnPostComment.setOnClickListener {

            runClassification(editTextComment.text.toString())
        }
    }
    private fun initWidgets(){

        editTextComment=binding.editTextComment
        btnPostComment=binding.btnPostComment
        txtOutput=binding.txtOutput
    }
    open fun runClassification(comment:String){


    }
}