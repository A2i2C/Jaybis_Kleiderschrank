package com.example.kleiderschrank.handler

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraHandler {
    lateinit var photoFile : File
        private set

    fun createCameraIntent(context: Context) : Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = createImageFile(context)
        val photoURI = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        return intent
    }

    private fun createImageFile(context: Context) : File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${timeStamp}_", ".jpg", storageDir
        )
    }
}