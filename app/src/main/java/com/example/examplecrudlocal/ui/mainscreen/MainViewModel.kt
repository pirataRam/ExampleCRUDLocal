package com.example.examplecrudlocal.ui.mainscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.rest.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _listPeople = MutableLiveData<List<Persona>>()
    val listPeople: LiveData<List<Persona>> get() = _listPeople

    fun getAll(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            _listPeople.postValue(roomRepository.getAllPersons(context))
        }
    }

}