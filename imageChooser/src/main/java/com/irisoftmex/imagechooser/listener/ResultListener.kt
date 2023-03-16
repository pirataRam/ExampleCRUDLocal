package com.irisoftmex.imagechooser.listener

internal interface ResultListener<T> {

    fun onResult(t: T?)
}