package com.example.examplecrudlocal.ui.mainscreen.fragments.peoplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.databinding.FragmentFirstBinding
import com.example.examplecrudlocal.rest.state.StatusType
import com.example.examplecrudlocal.tools.ARGS_EXTRAS
import com.example.examplecrudlocal.ui.base.BaseFragment
import com.example.examplecrudlocal.ui.mainscreen.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ListPersonsFragment : BaseFragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val vm: ListPersonsViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var listAdapter: ListPeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setListeners() {
        with(binding){
            fab.setOnClickListener {
                goToFragment(R.id.SecondFragment)
            }
        }
    }

    override fun setObservers() {
        with(vm) {
            listPeople.observe(viewLifecycleOwner) {
                val resource = it ?: return@observe
                mainActivity.showLoading(false)
                when (resource.statusType) {
                    StatusType.SUCCESS -> {
                        listAdapter = ListPeopleAdapter(it.data ?: emptyList()) { person, isErase ->
                            if (isErase) {
                                mainActivity.showQuestionMessage(
                                    getString(R.string.title_will_erase),
                                    getString(R.string.message_will_erase)
                                ) { confirm ->
                                    if (confirm)
                                        vm.deletePeople(requireContext(), person)
                                }
                            } else {
                                val b = Bundle()
                                b.putString(ARGS_EXTRAS, Gson().toJson(person))
                                goToFragment(R.id.SecondFragment, bundle = b)
                            }

                        }
                        binding.rvListPersons.adapter = listAdapter
                    }
                    StatusType.ERROR -> mainActivity.showErrorMessage(resource.message)
                    StatusType.LOADING -> mainActivity.showLoading(true)
                }
            }
            deleted.observe(viewLifecycleOwner){
                val resource = it ?: return@observe
                mainActivity.showLoading(false)
                when(resource.statusType){
                    StatusType.SUCCESS -> {
                        mainActivity.showToastMessage(getString(R.string.toast_inserted))
                        vm.loadListFromLocal(requireContext())
                    }
                    StatusType.ERROR -> mainActivity.showErrorMessage(resource.message)
                    StatusType.LOADING -> mainActivity.showLoading(true)
                }
            }
        }
}

    override fun cleanFields() {}

    override fun removeObservers() {
        with(vm) {
            listPeople.removeObservers(viewLifecycleOwner)
            deleted.removeObservers(viewLifecycleOwner)
        }
        _binding = null
    }

    override fun initViewComponents() {
        mainActivity = requireActivity() as MainActivity
        vm.loadListFromLocal(requireContext())
        binding.rvListPersons.layoutManager = LinearLayoutManager(requireContext()).apply { orientation = LinearLayoutManager.VERTICAL }
    }

    override fun changeToolbarParams() {
        mainActivity.changeToolbarParams(getString(R.string.first_fragment_label))
    }

}