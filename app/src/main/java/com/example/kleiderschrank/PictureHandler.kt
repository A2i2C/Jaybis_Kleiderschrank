package com.example.kleiderschrank

import android.graphics.*
import android.media.ExifInterface
import java.io.File
import kotlin.math.max

class PictureHandler {
    public fun processImage(photoFile: File): Bitmap {
        val bitmap = loadBitmap(photoFile)
        val rotate = rotateIfRequired(bitmap, photoFile)
        return scaleBitmap(rotate)

    }
    private fun loadBitmap(photoFile: File): Bitmap {
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return BitmapFactory.decodeFile(photoFile.absolutePath, options)
    }

    private fun rotateIfRequired(bitmap: Bitmap, photoFile: File): Bitmap {
        val matrix = Matrix()
        val exif = ExifInterface(photoFile.absolutePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap
        }

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }

    private fun scaleBitmap(bitmap: Bitmap, maxValue: Int = 512): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // first check if scaling is necessary
        if (width <= maxValue && height <= maxValue) {
            return bitmap
        }

        // calculate the scaling factor
        val ratio = minOf(
            maxValue.toFloat() / width,
            maxValue.toFloat() / height
        )
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun compressFile(photoFile: File, quality: Int = 80, bitmap: Bitmap) {
        photoFile.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }

    }


}