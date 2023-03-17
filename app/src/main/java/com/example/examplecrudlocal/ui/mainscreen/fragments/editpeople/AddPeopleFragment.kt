package com.example.examplecrudlocal.ui.mainscreen.fragments.editpeople

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.databinding.FragmentSecondBinding
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.rest.state.StatusType
import com.example.examplecrudlocal.tools.ARGS_EXTRAS
import com.example.examplecrudlocal.tools.Constants
import com.example.examplecrudlocal.tools.DIR_SAVE
import com.example.examplecrudlocal.tools.MAX_SIZE
import com.example.examplecrudlocal.tools.encodeBase64
import com.example.examplecrudlocal.tools.gone
import com.example.examplecrudlocal.tools.loadImage
import com.example.examplecrudlocal.tools.show
import com.example.examplecrudlocal.ui.base.BaseFragment
import com.example.examplecrudlocal.ui.mainscreen.MainActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.irisoftmex.imagechooser.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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
    private var imagePath: Uri? = null
    private val imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null){
            imagePath = result.data?.data
            Log.d("Fragment", "Uri es -> ${imagePath.toString()}")
            Log.d("Fragment", "Uri es -> ${imagePath?.path}")
            Log.d("Fragment", "Uri es -> ${imagePath?.encodedPath}")
            Glide.with(requireContext())
                .load(imagePath)
                .placeholder(R.drawable.ic_sand_clock)
                .error(R.drawable.ic_no_photo)
                .centerCrop()
                .into(binding.acivPhotoFull)
        } else {
            activityMain.showToastMessage(getString(R.string.error_no_photo_selected))
        }
    }

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
        //validating if person is not null to show read only
        if (persona != null){
            enableFields(false)
            with(binding){
                tietName.setText(persona?.nombre)
                tietAge.setText(persona?.edad.toString())
                tietAddress.setText(persona?.domicilioCalle)
                tietNumberInt.setText(persona?.domicilioNumInt)
                tietNumberExt.setText(persona?.domicilioNumExt)
                tietSuburb.setText(persona?.domicilioColonia)
                mactvEntity.setText(persona?.domicilioEntidad)
                mactvLocale.setText(persona?.domicilioMunicipio)
                lifecycleScope.launch(Dispatchers.Main){
                    acivPhotoFull.loadImage(persona!!.fotografia)
                }
                acbSave.gone()
            }
        }
    }

    override fun setListeners() {
        with(binding){
            fabEdit.setOnClickListener {
                fabEdit.gone()
                acbSave.show()
                activityMain.changeToolbarParams(String.format(getString(R.string.second_fragment_label), getString(R.string.fragment_label_update)))
                acbSave.text = getString(R.string.btn_update)
                enableFields(true)
                tietName.requestFocus()
            }
            acbSave.setOnClickListener {
                if (imagePath == null && persona == null)
                    activityMain.showToastMessage(getString(R.string.error_no_photo_selected))
                else
                    vm.validateEntities(binding.mactvEntity, binding.mactvLocale, listLocales)
            }
            mactvEntity.setOnItemClickListener { _, _, position, _ ->
                listLocales = Constants.getMunicipiosByEstado(listEntities.getItem(position)!!)
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listLocales)
                mactvLocale.setAdapter(adapter)
            }
            acivPhotoFull.setOnClickListener {
                activityMain.showQuestionMessage(){accept ->
                    if (accept) { //Camera
                        ImagePicker.Companion.with(requireActivity())
                            .cameraOnly()
                            .cropSquare()
                            .compress(MAX_SIZE)
                            //  Path: /storage/sdcard0/Android/data/package/files
                            .saveDir(requireContext().getExternalFilesDir(null)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/DCIM
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Download
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Pictures
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
                            .saveDir(
                                File(
                                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!,
                                    DIR_SAVE
                                )
                            )
                            //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
                            .saveDir(requireContext().getExternalFilesDir(DIR_SAVE)!!)
                            //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
                            .saveDir(File(requireContext().externalCacheDir, DIR_SAVE))
                            //  Path: /data/data/package/cache/ImagePicker
                            .saveDir(File(requireContext().cacheDir, DIR_SAVE))
                            //  Path: /data/data/package/files/ImagePicker
                            .saveDir(File(requireContext().filesDir, DIR_SAVE))
                            .createIntent { intent ->
                                imagePicker.launch(intent)
                            }
                    } else { //Gallery
                        ImagePicker.Companion.with(requireActivity())
                            .galleryOnly()
                            .cropSquare()
                            .galleryMimeTypes(  //Exclude gif images
                                mimeTypes = arrayOf(
                                    "image/jpg",
                                    "image/jpeg",
                                    "image/png"
                                )
                            )
                            .compress(MAX_SIZE)
                            //  Path: /storage/sdcard0/Android/data/package/files
                            .saveDir(requireContext().getExternalFilesDir(null)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/DCIM
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Download
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Pictures
                            .saveDir(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                            //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
                            .saveDir(
                                File(
                                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!,
                                    DIR_SAVE
                                )
                            )
                            //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
                            .saveDir(requireContext().getExternalFilesDir(DIR_SAVE)!!)
                            //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
                            .saveDir(File(requireContext().externalCacheDir, DIR_SAVE))
                            //  Path: /data/data/package/cache/ImagePicker
                            .saveDir(File(requireContext().cacheDir, DIR_SAVE))
                            //  Path: /data/data/package/files/ImagePicker
                            .saveDir(File(requireContext().filesDir, DIR_SAVE))
                            .createIntent { intent ->
                                imagePicker.launch(intent)
                            }
                    }
                }
            }
            mactvEntity.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    nsvMain.smoothScrollTo(nsvMain.scrollX + 100, nsvMain.scrollY, 2000)
            }
            mactvLocale.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    nsvMain.smoothScrollTo(nsvMain.scrollX + 100, nsvMain.scrollY, 2000)
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
                        val file = File(imagePath?.path.toString())
                        if (file.exists()) {
                            val encoded = file.readBytes().encodeBase64()
                            val personTemp = Persona(
                                nombre = tietName.text.toString().trim(),
                                edad = tietAge.text.toString().toInt(),
                                domicilioCalle = tietAddress.text.toString().trim(),
                                domicilioNumInt = tietNumberInt.text.toString().trim(),
                                domicilioNumExt = tietNumberExt.text.toString().ifEmpty { "0" },
                                domicilioColonia = tietSuburb.text.toString().trim(),
                                domicilioEntidad = mactvEntity.text.toString().trim(),
                                domicilioMunicipio = mactvLocale.text.toString().trim(),
                                fotografia = encoded
                            )
                            if (persona != null)
                                vm.updatePerson(requireContext(), personTemp)
                            else
                                vm.addPerson(requireContext(), personTemp)
                        }
                    }
                }
            }
            saving.observe(viewLifecycleOwner){
                val resource = it ?: return@observe
                activityMain.showLoading(false)
                when(resource.statusType){
                    StatusType.SUCCESS -> {
                        activityMain.showToastMessage(getString(R.string.toast_inserted))
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
                        cleanFields()
                    }
                    StatusType.ERROR -> activityMain.showErrorMessage(resource.message)
                    StatusType.LOADING -> activityMain.showLoading(true)
                }
            }
            listItem.observe(viewLifecycleOwner){view ->
                if (view == null)
                    vm.validateFields(list)
                else {
                    with(view){
                        text = null
                        error =  getString(R.string.error_no_valid_field)
                        requestFocus()
                    }
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
            imagePath = null
            findNavController().navigateUp()
        }
    }

    override fun removeObservers() {
        with(vm){
            listErrors.removeObservers(viewLifecycleOwner)
            saving.removeObservers(viewLifecycleOwner)
            update.removeObservers(viewLifecycleOwner)
            listItem.removeObservers(viewLifecycleOwner)
        }
        _binding = null
    }

    override fun initViewComponents() {
        activityMain = requireActivity() as MainActivity
        with(binding) {
            list = listOf(tietName, tietAge, tietAddress, tietNumberInt, tietNumberExt, tietSuburb)
            list2 = listOf(mactvEntity, mactvLocale)
            enableFields(true)
            mactvEntity.setAdapter(listEntities)
        }
    }

    override fun changeToolbarParams() {
        activityMain.changeToolbarParams(String.format(getString(R.string.second_fragment_label), if (persona == null) getString(R.string.fragment_label_add) else getString(R.string.fragment_label_update)))
        binding.acbSave.text = if (persona == null) getString(R.string.btn_save) else getString(R.string.btn_update)
    }

    private fun enableFields(b :Boolean) {
        with(binding){
            for (i in list){
                with(i) {
                    isEnabled = b
                }
            }
            for (j in list2){
                with(j){
                    isEnabled = b
                }
            }
            acivPhotoFull.isClickable = b
            acbSave.text = getString(if (persona == null) R.string.btn_save else R.string.btn_update)
        }
    }

}