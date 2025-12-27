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
import com.example.kleiderschrank.datahandling.dao.KleidungDao
import com.example.kleiderschrank.datahandling.database.Kleiderschrank
import com.example.kleiderschrank.datahandling.entity.Kleidung
import com.example.kleiderschrank.handler.CameraHandler
import com.example.kleiderschrank.handler.PictureHandler

class MainActivity : AppCompatActivity() {

    private lateinit var btnCamera: Button
    private var cameraHandler: CameraHandler = CameraHandler()
    private var pictureHandler: PictureHandler = PictureHandler()
    private lateinit var image: ImageView
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    lateinit var db: Kleiderschrank
    lateinit var kleidungDao: KleidungDao


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
        db = Kleiderschrank.getDatabase(this)
        kleidungDao = db.kleidungDao()
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
                val kleidung = Kleidung(
                    bildPfad = cameraHandler.photoFile.absolutePath)
                Thread {
                    kleidungDao.insert(kleidung)
                    val alleKleidungsstuecke = kleidungDao.getAll()
                    for (item in alleKleidungsstuecke) {
                        Log.i("Datenbank Inhalt", "ID: ${item.id}, Name: ${item.name}, BildPfad: ${item.bildPfad}")
                    }
                }.start()
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

