package com.example.mlkit.helper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mlkit.R
import com.example.mlkit.databinding.ActivityAudioHelperBinding

public open class AudioHelperActivity : AppCompatActivity() {
    lateinit var binding:ActivityAudioHelperBinding
    private var permissionsDenied = false
    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    private val requestCode = 1
    private val REQUEST_AUDIO_PERMISSION = 2

    open lateinit var outputTextView:TextView
    open lateinit var specsTextView: TextView
    open lateinit var startRecordingButton:Button
    open lateinit var stopRecordingButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAudioHelperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWidgets()
        checkAndRequestPermissions()

        stopRecordingButton.isEnabled=false
        if (areAllPermissionsGranted()){

            startRecordingButton.setOnClickListener {
                startRecordingButton.isEnabled=false
                stopRecordingButton.isEnabled=true
            }
            stopRecordingButton.setOnClickListener {
                startRecordingButton.isEnabled=true
                stopRecordingButton.isEnabled=false
            }



        }else if (!areAllPermissionsGranted()){
            checkAndRequestPermissions()

        }
    }
    open fun initWidgets(){

        outputTextView=binding.output
        specsTextView=binding.specs
        startRecordingButton=binding.startRecording
        stopRecordingButton=binding.audioBtnStop
    }
    private fun areAllPermissionsGranted(): Boolean {
        for (permission in permissions) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    private fun checkAndRequestPermissions() {
        val ungrantedPermissions = ArrayList<String>()

        for (permission in permissions) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ungrantedPermissions.add(permission)
            }
        }

        if (ungrantedPermissions.isNotEmpty()) {
            val permissionsArray = ungrantedPermissions.toTypedArray()
            ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("PRABHAT", "onRequestPermissionsResult: ${grantResults.toString()}")
        if (requestCode == this.requestCode) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Log.d("PRABHAT", "onRequestPermissionsResult:granted ")

                } else {
                    // Permission denied
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    Log.d("PRABHAT", "onRequestPermissionsResult: Permission denied")
                    permissionsDenied = true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionsDenied) {
            // Check for permission status again and handle it
            checkAndRequestPermissions()
            permissionsDenied = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}