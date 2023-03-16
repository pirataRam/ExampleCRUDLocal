package com.example.examplecrudlocal.ui.mainscreen.fragments.peoplelist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.databinding.FragmentFirstBinding
import com.example.examplecrudlocal.rest.state.StatusType
import com.example.examplecrudlocal.tools.ARGS_EXTRAS
import com.example.examplecrudlocal.ui.base.BaseFragment
import com.example.examplecrudlocal.ui.mainscreen.MainActivity
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
    private lateinit var listAdapter: PeopleListAdapter

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

    }

    override fun setObservers() {
        vm.listPeople.observe(viewLifecycleOwner){
            val resource = it ?: return@observe
            mainActivity.showLoading(false)
            when(resource.statusType){
                StatusType.SUCCESS -> {
                    listAdapter = PeopleListAdapter(it.data ?: emptyList()){ person, isErase ->
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
                            b.putParcelable(ARGS_EXTRAS, person)
                            goToFragment(R.id.SecondFragment, bundle = b)
                        }

                    }
                }
                StatusType.ERROR -> mainActivity.showErrorMessage(resource.message)
                StatusType.LOADING -> mainActivity.showLoading(true)
            }
        }
    }

    override fun cleanFields() {}

    override fun removeObservers() {
        vm.listPeople.removeObservers(viewLifecycleOwner)
        _binding = null
    }

    override fun initViewComponents() {
        mainActivity = requireActivity() as MainActivity
        vm.loadListFromLocal(requireContext())
    }

    override fun changeToolbarParams() {

    }

}