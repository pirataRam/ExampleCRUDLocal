package com.example.examplecrudlocal.rest.state

enum class StatusType {
    SUCCESS,
    ERROR,
    LOADING;

    fun isLoading() = this == LOADING
}