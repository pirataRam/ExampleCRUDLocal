package com.example.examplecrudlocal.localdb.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.examplecrudlocal.tools.TABLE_PEOPLE

@Entity(tableName = TABLE_PEOPLE)
data class Persona (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    var nombre: String,
    var edad: Int,
    var domicilioCalle: String,
    var domicilioNumInt: String,
    var domicilioNumExt: String,
    var domicilioColonia: String,
    var domicilioEntidad: String,
    var domicilioMunicipio: String,
    var fotografia: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeInt(edad)
        parcel.writeString(domicilioCalle)
        parcel.writeString(domicilioNumInt)
        parcel.writeString(domicilioNumExt)
        parcel.writeString(domicilioColonia)
        parcel.writeString(domicilioEntidad)
        parcel.writeString(domicilioMunicipio)
        parcel.writeString(fotografia)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Persona> {
        override fun createFromParcel(parcel: Parcel): Persona {
            return Persona(parcel)
        }

        override fun newArray(size: Int): Array<Persona?> {
            return arrayOfNulls(size)
        }
    }
}
