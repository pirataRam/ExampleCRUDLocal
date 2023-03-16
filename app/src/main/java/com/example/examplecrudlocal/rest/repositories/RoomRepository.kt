package com.example.examplecrudlocal.rest.repositories

import android.content.Context
import com.example.examplecrudlocal.localdb.db.AppDatabase
import com.example.examplecrudlocal.localdb.entities.Persona
import javax.inject.Inject

class RoomRepository @Inject constructor(){

    suspend fun getAllPersons(context: Context): List<Persona> = AppDatabase.instance(context = context).personaDao().getAll()

    suspend fun insertPerson(context: Context, persona: Persona) = AppDatabase.instance(context = context).personaDao().insert(persona = persona)

    suspend fun updatePerson(context: Context, persona: Persona) = AppDatabase.instance(context = context).personaDao().update(persona = persona)

    suspend fun deletePerson(context: Context, persona: Persona) = AppDatabase.instance(context = context).personaDao().delete(persona = persona)

}