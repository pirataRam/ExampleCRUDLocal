package com.example.examplecrudlocal.ui.mainscreen.fragments.peoplelist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.rest.repositories.RoomRepository
import com.example.examplecrudlocal.rest.state.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPersonsViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _listPeople = MutableLiveData<Resource<List<Persona>>>()
    val listPeople: LiveData<Resource<List<Persona>>> get() = _listPeople

    private val _deleted = MutableLiveData<Resource<Boolean>>()
    val deleted: LiveData<Resource<Boolean>> get() = _deleted

    fun loadListFromLocal(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            _listPeople.postValue(Resource.loading())
            _listPeople.postValue(Resource.success(roomRepository.getAllPersons(context = context)))
        }
    }

    fun deletePeople(context: Context, persona: Persona) {
        viewModelScope.launch(Dispatchers.IO){
            _deleted.postValue(Resource.loading())
            try {
                roomRepository.deletePerson(context = context, persona = persona)
                _deleted.postValue(Resource.success(true))
            } catch (e: Exception){
                _deleted.postValue(Resource.error(context.getString(R.string.error_saving_field)))
            }
        }
    }

}