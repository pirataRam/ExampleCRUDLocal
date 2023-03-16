package com.example.examplecrudlocal.ui.mainscreen.fragments.editpeople

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.databinding.FragmentSecondBinding
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.rest.state.StatusType
import com.example.examplecrudlocal.tools.ARGS_EXTRAS
import com.example.examplecrudlocal.tools.Constants
import com.example.examplecrudlocal.ui.base.BaseFragment
import com.example.examplecrudlocal.ui.mainscreen.MainActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class AddPeopleFragment : BaseFragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val vm: AddPeopleViewModel by viewModels()
    private lateinit var activityMain: MainActivity
    private var persona: Persona? = null
    private var list: List<TextInputEditText> = emptyList()
    private var list2: List<MaterialAutoCompleteTextView> = emptyList()
    private val listEntities by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.estados)
    }
    private var listLocales = emptyList<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        persona = try {
            val t = requireArguments().getString(ARGS_EXTRAS)
            if (t != null)
                Gson().fromJson(t, Persona::class.java)
            else
                null
        } catch (e: Exception){
            null
        }
    }

    override fun setListeners() {
        with(binding){
            acbSave.setOnClickListener {
                vm.validateFields(list)
            }
            mactvEntity.setOnItemClickListener { parent, view, position, id ->
                listLocales = Constants.getMunicipiosByEstado(listEntities.getItem(position)!!)
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listLocales)
                mactvLocale.setAdapter(adapter)
            }
        }
    }

    override fun setObservers() {
        with(vm){
            listErrors.observe(viewLifecycleOwner){
                val list = it ?: return@observe
                if (list.isNotEmpty()) {
                    for (i in list) {
                        i.error = getString(R.string.error_empty_field)
                        i.requestFocus()
                    }
                } else {
                    with(binding){
                        val personTemp = Persona (
                            nombre = tietName.text.toString(),
                            edad = tietAge.text.toString().toInt(),
                            domicilioCalle = tietAddress.text.toString(),
                            domicilioNumInt = tietNumberInt.text.toString(),
                            domicilioNumExt = tietNumberExt.text.toString().ifEmpty { "0" },
                            domicilioColonia = tietSuburb.text.toString(),
                            domicilioEntidad = mactvEntity.text.toString(),
                            domicilioMunicipio = mactvLocale.text.toString(),
                            fotografia = ""
                        )
                        if (persona != null)
                            vm.updatePerson(requireContext(), personTemp)
                        else
                            vm.addPerson(requireContext(), personTemp)
                    }
                }
            }
            saving.observe(viewLifecycleOwner){
                val resource = it ?: return@observe
                activityMain.showLoading(false)
                when(resource.statusType){
                    StatusType.SUCCESS -> {
                        activityMain.showToastMessage(getString(R.string.toast_inserted))
                        activityMain.changeFabIcon(false)
                        cleanFields()
                    }
                    StatusType.ERROR -> activityMain.showErrorMessage(resource.message)
                    StatusType.LOADING -> activityMain.showLoading(true)
                }
            }
            update.observe(viewLifecycleOwner){
                val resource = it ?: return@observe
                activityMain.showLoading(false)
                when(resource.statusType){
                    StatusType.SUCCESS -> {
                        activityMain.showToastMessage(getString(R.string.toast_updated))
                        activityMain.changeFabIcon(false)
                        cleanFields()
                    }
                    StatusType.ERROR -> activityMain.showErrorMessage(resource.message)
                    StatusType.LOADING -> activityMain.showLoading(true)
                }
            }
        }
    }

    override fun cleanFields() {
        with(binding){
            tietName.text = null
            tietAge.text = null
            tietAddress.text = null
            tietNumberInt.text = null
            tietNumberExt.text = null
            tietSuburb.text = null
            findNavController().navigateUp()
        }
    }

    override fun removeObservers() {
        with(vm){
            listErrors.removeObservers(viewLifecycleOwner)
            saving.removeObservers(viewLifecycleOwner)
            update.removeObservers(viewLifecycleOwner)
        }
        _binding = null
    }

    override fun initViewComponents() {
        activityMain = requireActivity() as MainActivity
        with(binding) {
            list = listOf(tietName, tietAge, tietAddress, tietNumberInt, tietNumberExt, tietSuburb)
            list2 = listOf(mactvEntity, mactvLocale)

            activityMain.setViewMode(persona != null){
                enableFields(it)
            }

            if (persona != null) {
                activityMain.changeFabIcon(true)
            } else {
                enableFields(true)
                mactvEntity.setAdapter(listEntities)
            }
        }
    }

    override fun changeToolbarParams() {
        activityMain.changeToolbarParams(String.format(getString(R.string.second_fragment_label), if (persona == null) getString(R.string.fragment_label_add) else getString(R.string.fragment_label_update)))
        binding.acbSave.text = if (persona == null) getString(R.string.btn_save) else getString(R.string.btn_update)
    }

    private fun enableFields(enable :Boolean) {
        with(binding){
            for (i in list){
                with(i) {
                    isFocusable = enable
                    isClickable = enable
                    isCursorVisible = enable
                }
            }
            for (j in list2){
                with(j){
                    isClickable = enable
                    isFocusable = enable
                    isCursorVisible = false
                }
            }
            acbSave.text = getString(if (persona == null) R.string.btn_save else R.string.btn_update)
        }
    }

}