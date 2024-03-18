package com.example.mlkit.image

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mlkit.databinding.ActivityImageClassificationBinding
import com.google.android.material.textview.MaterialTextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class ImageClassificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageClassificationBinding
    private lateinit var imageView: ImageView
    private lateinit var outputTextView: MaterialTextView
    private lateinit var buttonPickImage: Button
    private lateinit var buttonStartCamera: Button


    private lateinit var imageLabeler: ImageLabeler


    private var permissionsDenied = false

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissions = arrayOf(

        Manifest.permission.READ_EXTERNAL_STORAGE

    )
    private val requestCode = 1
    private val REQUEST_PICK_IMAGE = 2
    private val REQUEST_CAPTURE_IMAGE = 3

    private lateinit var photoFile: File

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWidgets()
        checkAndRequestPermissions()
        if (areAllPermissionsGranted()) {

            binding.buttonPickImage.setOnClickListener {

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
            binding.buttonStartCamera.setOnClickListener {
                onStartCamera()
            }


//            Toast.makeText(this, "all permission granted", Toast.LENGTH_SHORT).show()
        } else if (!areAllPermissionsGranted()) {
            Log.d("PRABHAT", "onCreate: permission not granted")
            checkAndRequestPermissions()
        }
    }

    private fun initWidgets() {
        imageView = binding.imageView
        outputTextView = binding.textViewOutput
        buttonPickImage = binding.buttonPickImage
        buttonStartCamera = binding.buttonStartCamera
        imageLabeler =
            ImageLabeling.getClient(
                ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build()
            )


    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun areAllPermissionsGranted(): Boolean {
        for (permission in permissions) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.S)
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

    @RequiresApi(Build.VERSION_CODES.S)
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE) {

                val uri = data?.data
                val bitmap = uri?.let { loadFromUri(it) }
                binding.imageView.setImageBitmap(bitmap)
                runClassification(bitmap!!)
            } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
                Log.d(TAG, "onActivityResult: Receive image from camera")
                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
                runClassification(bitmap!!)
            }

        }
    }

    private fun loadFromUri(uri: Uri): Bitmap? {

        var bitmap: Bitmap? = null

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            bitmap = ImageDecoder.decodeBitmap(source)
        } else {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
        return bitmap


    }

    /*private fun runClassification(bitmap: Bitmap){
        val inputImage= InputImage.fromBitmap(bitmap,0)
        imageLabeler.process(inputImage).addOnSuccessListener {
            Log.d(TAG, "runClassification: success")
            OnSuccessListener<List<ImageLabel>> { imageLabels ->
                Log.d(TAG, "runClassification: $imageLabels")
                if (imageLabels?.size!! > 0) {
                    Log.d(TAG, "runClassification: imageLabels?.size!! > 0")


                    var builder=""
                    for (labels in imageLabels){
                        Log.d(TAG, "runClassification: ${labels.text}")
                        builder+=labels.text
                        builder+=":"
                        builder+=labels.confidence
                        builder+="\n"
                    }
                    Log.d(TAG, "runClassification: $builder")
                    binding.textViewOutput.text=builder
                } else {

                    binding.textViewOutput.text="Unable classified"
                }
            }
        }.addOnFailureListener {
            Log.d(TAG, "runClassification: ")
            *//*p0 -> p0.stackTrace
        *//*
        }
    }*/
    open fun runClassification(bitmap: Bitmap) {
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
                    binding.textViewOutput.text = builder
                } else {
                    binding.textViewOutput.text = "Unable to classify"
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "runClassification: Failed to process image", e)
                // Handle failure, e.g., show an error message
            }
    }

    private fun onStartCamera() {
        //create a file to share with camera
        photoFile = createPhotoFile()
        val fileUri = FileProvider.getUriForFile(this, "com.iago.fileProvider", photoFile)

        //create an intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        //startActivity for result
//        startActivityForResult(intent,REQUEST_CAPTURE_IMAGE)
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        }
    }

    private fun createPhotoFile(): File {
        val photoFileDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ML_IMAGE_HELPER")

        if (!photoFileDir.exists()) {

            photoFileDir.mkdirs()
        }
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(photoFileDir.path + File.separator + fileName)
    }

    companion object {
        const val TAG = "PRABHAT"
    }


}