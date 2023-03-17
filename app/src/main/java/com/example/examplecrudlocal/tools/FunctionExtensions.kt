package com.example.examplecrudlocal.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.examplecrudlocal.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.*

suspend fun ByteArray.byteToBitmap(): Bitmap = withContext(Dispatchers.IO) {
    BitmapFactory.decodeByteArray(this@byteToBitmap, 0, this@byteToBitmap.size)
}

suspend fun AppCompatImageView.loadImage(imageEncoded: String) = withContext(Dispatchers.Main) {
    val decoded = imageEncoded.decodeBase64()
    val bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
    Glide.with(this@loadImage)
        .load(bitmap)
        .centerCrop()
        .placeholder(R.drawable.ic_sand_clock)
        .error(R.drawable.ic_no_photo)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this@loadImage)
}

fun String.decodeBase64(): ByteArray {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        Base64.getDecoder().decode(this)
    else
        android.util.Base64.decode(this, android.util.Base64.DEFAULT)
}

fun ByteArray.encodeBase64(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        Base64.getEncoder().encodeToString(this)
    else
        android.util.Base64.encodeToString(this, android.util.Base64.DEFAULT)
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}