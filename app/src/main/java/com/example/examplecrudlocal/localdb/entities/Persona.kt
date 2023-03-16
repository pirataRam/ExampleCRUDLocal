package com.example.examplecrudlocal.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.examplecrudlocal.tools.TABLE_PEOPLE

@Entity(tableName = TABLE_PEOPLE)
data class Persona (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var nombre: String,
    var edad: Int,
    var domicilioCalle: String,
    var domicilioNumInt: String,
    var domicilioNumExt: String,
    var domicilioColonia: String,
    var domicilioEntidad: String,
    var domicilioMunicipio: String,
    var fotografia: String
) : java.io.Serializable
