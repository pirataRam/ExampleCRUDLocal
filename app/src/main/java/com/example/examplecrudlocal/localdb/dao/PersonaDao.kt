package com.example.examplecrudlocal.localdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.tools.TABLE_PEOPLE

@Dao
interface PersonaDao {

    @Query("SELECT * FROM $TABLE_PEOPLE")
    suspend fun getAll(): List<Persona>

    @Insert
    suspend fun insert(persona: Persona)

    @Update
    suspend fun update(persona: Persona)

    @Delete
    suspend fun delete(persona: Persona)

}