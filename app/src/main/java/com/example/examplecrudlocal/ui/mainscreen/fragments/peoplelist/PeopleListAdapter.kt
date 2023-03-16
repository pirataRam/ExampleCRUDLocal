package com.example.examplecrudlocal.ui.mainscreen.fragments.peoplelist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.examplecrudlocal.databinding.ItemPersonBinding
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.tools.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PeopleListAdapter (
    private val listPeople: List<Persona>,
    private val onTouch: (onClickPerson: Persona, willDelete: Boolean) -> Unit
        ) : RecyclerView.Adapter<PeopleListAdapter.PeopleViewHolder>(){

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
                        CoroutineScope(Dispatchers.Main).launch {
                            acivPhoto.loadImage(persona.fotografia)
                        }
                    }
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder =
        PeopleViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount(): Int = listPeople.size

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.setData(listPeople[position])
    }
}