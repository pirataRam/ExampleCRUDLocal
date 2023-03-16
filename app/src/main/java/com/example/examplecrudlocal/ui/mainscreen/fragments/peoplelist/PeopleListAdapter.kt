package com.example.examplecrudlocal.ui.mainscreen.fragments.peoplelist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.examplecrudlocal.databinding.ItemPersonBinding
import com.example.examplecrudlocal.localdb.entities.Persona

class PeopleListAdapter (
    private val listPeople: List<Persona>,
    private val onTouch: (onClickPerson: Persona, willDelete: Boolean) -> Unit
        ) : RecyclerView.Adapter<>(){

    inner class PeopleViewHolder(private val binding: ItemPersonBinding):
            RecyclerView.ViewHolder(binding.root) {

                @SuppressLint("SetTextI18n")
                fun setData(persona: Persona){
                    with(binding){
                        acivDelete.setOnClickListener { _ ->
                            onTouch(persona, true)
                        }
                        clRoot.setOnClickListener { _ ->
                            onTouch(persona, false)
                        }
                        actvLabelName.text = persona.nombre
                        actvLabelAge.text = persona.edad.toString()
                        actvLabelAddress.text = ("${persona.domicilioCalle} ${persona.domicilioNumInt} ${if (persona.domicilioNumExt != "0") "Ext. ${persona.domicilioNumExt} " else ""}, ${persona.domicilioColonia}, ${persona.domicilioMunicipio}, ${persona.domicilioEntidad}")
                    }

                }

            }
}