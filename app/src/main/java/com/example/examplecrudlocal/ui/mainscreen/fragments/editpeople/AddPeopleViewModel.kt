package com.example.examplecrudlocal.ui.mainscreen.fragments.editpeople

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.rest.repositories.RoomRepository
import com.example.examplecrudlocal.rest.state.Resource
import com.example.examplecrudlocal.tools.Constants
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPeopleViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _saving = MutableLiveData<Resource<Boolean>>()
    val saving: LiveData<Resource<Boolean>> get() = _saving

    private val _update = MutableLiveData<Resource<Boolean>>()
    val update: LiveData<Resource<Boolean>> get() = _update

    private val _listErrors = MutableLiveData<List<TextInputEditText>>()
    val listErrors: LiveData<List<TextInputEditText>> get() = _listErrors

    private val _listItem = MutableLiveData<MaterialAutoCompleteTextView?>()
    val listItem: LiveData<MaterialAutoCompleteTextView?> get() = _listItem

    fun validateFields(list: List<TextInputEditText>){
        val array = ArrayList<TextInputEditText>()
        for(i in list){
            if (i.text.toString().trim().isEmpty())
                array.add(i)
        }
        _listErrors.postValue(array.toList())
    }

    fun validateEntities(entity: MaterialAutoCompleteTextView, locale: MaterialAutoCompleteTextView, locales: List<String>){
        var t = ""
        var textWorking = entity.text.toString().trim()
        //validating entities
        for (i in Constants.estados){
            if (i == textWorking){
                t = i
                break
            }
        }
        if (t.isEmpty())
            _listItem.postValue(entity)
        else {
            t = ""
            textWorking = locale.text.toString().trim()
            //validating a locale
            for(j in locales){
                if (j == textWorking){
                    t = j
                    break
                }
            }
        }
        _listItem.postValue(if (t.isEmpty()) locale else null)
    }

    fun addPerson(context: Context, persona: Persona){
        viewModelScope.launch(Dispatchers.IO){
            _saving.postValue(Resource.loading())
            try {
                roomRepository.insertPerson(context = context, persona = persona)
                _saving.postValue(Resource.success(true))
            } catch (e: Exception){
                _saving.postValue(Resource.error(context.getString(R.string.error_saving_field)))
            }
        }
    }

    fun updatePerson(context: Context, persona: Persona){
        viewModelScope.launch(Dispatchers.IO){
            _update.postValue(Resource.loading())
            try {
                roomRepository.updatePerson(context = context, persona = persona)
                _update.postValue(Resource.success(true))
            } catch (e: Exception){
                _update.postValue(Resource.error(context.getString(R.string.error_saving_field)))
            }
        }
    }

}