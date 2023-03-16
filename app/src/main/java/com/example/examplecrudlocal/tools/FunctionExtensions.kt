package com.example.examplecrudlocal.tools

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun AppCompatImageView.loadImage(stringCoded: String){
    GlobalScope.launch(Dispatchers.Main){
        Glide.with(this@loadImage)
    }
}

fun String.encodeBase64()