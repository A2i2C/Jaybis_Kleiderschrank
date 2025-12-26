package com.example.kleiderschrank.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kleiderschrank.R
import com.example.kleiderschrank.handler.CameraHandler
import com.example.kleiderschrank.handler.PictureHandler

class MainActivity : AppCompatActivity() {

    private lateinit var btnCamera: Button
    private var cameraHandler: CameraHandler = CameraHandler()
    private var pictureHandler: PictureHandler = PictureHandler()
    private lateinit var image: ImageView
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnCamera = findViewById(R.id.btnCamera)
        image = findViewById(R.id.testView)
        initCameraLaunchers()
        btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun openCamera() {
        val cameraIntent = cameraHandler.createCameraIntent(this)
        cameraLauncher.launch(cameraIntent)
    }

    private fun initCameraLaunchers() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                Log.i("Image saved", "Image saved at: ${cameraHandler.photoFile.absolutePath}")
                val processedBitmap = pictureHandler.processImage(cameraHandler.photoFile)
                pictureHandler.compressFile(cameraHandler.photoFile, bitmap = processedBitmap)
                image.setImageBitmap(processedBitmap)
            }
        }
        cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        { granted ->
            if (granted) {
                openCamera()
            }
        }
    }



}